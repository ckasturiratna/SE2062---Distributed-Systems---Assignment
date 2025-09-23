# Common Time Utilities

Utilities for consistent time synchronisation and logical ordering across distributed system components.

## What it does
- Samples configured NTP servers, aggregates offsets, and exposes health and jitter metrics.
- Provides a thread-safe Lamport logical clock and `OrderedTimestamp` composite for deterministic ordering.
- Surfaces lightweight metric suppliers for integration with Micrometer or other monitoring stacks.

## Getting started
```java
TimeSyncConfig config = TimeSyncConfig.defaults();
TimeSyncService timeSync = new TimeSyncService(config, new NtpClient());
timeSync.start();

LamportClock clock = new LamportClock();
OrderedTimestamp ts = OrderedTimestamp.from(clock, timeSync);
```

When handling an incoming message:
```java
clock.update(incomingTs.lamport());
OrderedTimestamp next = OrderedTimestamp.from(clock, timeSync);
```

Call `timeSync.stop()` during shutdown to release the scheduler thread.

## Safety notes
- The service never adjusts the host system clock; offsets are applied logically at read time.
- If all NTP requests fail, the service degrades gracefully, retains prior samples when available, and exposes health via `TimeSyncHealth`.
- Median aggregation and jitter checks filter outliers; callers can fall back to system time if `health().status()` is `UNAVAILABLE`.
