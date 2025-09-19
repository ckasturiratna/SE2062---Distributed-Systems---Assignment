# Distributed File Storage System

A Java-based distributed file storage system demonstrating core distributed systems concepts including fault tolerance, replication, consistency, time synchronization, and consensus algorithms.

## Architecture Overview

This system consists of the following modules:

### 1. Common Module
- **Shared DTOs**: `FileMetadata`, `NodeInfo`, `FileLocation`
- **Time Synchronization**: Logical and Vector clock implementations
- **Utilities**: ID generation, checksums, validation helpers

### 2. Metadata Service
- **Technology**: Apache Ratis for Raft consensus
- **Purpose**: Manages file metadata and location information
- **Features**: 
  - Consistent metadata storage across cluster
  - Leader election and fault tolerance
  - REST API for metadata operations

### 3. Storage Node
- **Technology**: gRPC for communication, JGroups for clustering
- **Purpose**: Stores actual file chunks with replication
- **Features**:
  - Automatic failure detection
  - Data replication across nodes
  - Load balancing and health monitoring

### 4. API Gateway
- **Technology**: Spring Boot REST API
- **Purpose**: Client-facing interface for file operations
- **Features**:
  - File upload/download/delete operations
  - Integration with metadata and storage services
  - Load balancing across storage nodes

### 5. Client CLI
- **Technology**: PicoCLI for command-line interface
- **Purpose**: Command-line tool for interacting with the system
- **Commands**: `upload`, `download`, `delete`, `list`, `info`

### 6. Test Harness
- **Technology**: JUnit 5, TestContainers
- **Purpose**: Integration and load testing
- **Features**: Multi-node testing, fault injection, performance benchmarks

## System Features

### Distributed Systems Concepts Demonstrated

1. **Consensus**: Raft consensus algorithm via Apache Ratis for metadata consistency
2. **Replication**: Configurable replication factor for data redundancy
3. **Fault Tolerance**: Automatic failure detection and recovery
4. **Time Synchronization**: Logical and Vector clocks for event ordering
5. **Consistency**: Strong consistency for metadata, eventual consistency for data
6. **Load Balancing**: Distributed file chunks across storage nodes

### Key Capabilities

- **File Operations**: Upload, download, delete files with metadata tracking
- **Replication**: Configurable replication factor (1-N)
- **Fault Recovery**: Automatic recovery from node failures
- **Scalability**: Horizontal scaling of storage nodes
- **Monitoring**: Health checks and load monitoring

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Docker and Docker Compose (for multi-node testing)

### Building the System

```bash
# Build all modules
mvn clean package

# Build Docker images
docker-compose build
```

### Running with Docker Compose

```bash
# Start the complete system (3 metadata nodes, 3 storage nodes, 1 API gateway)
docker-compose up -d

# Check system status
docker-compose ps

# View logs
docker-compose logs -f api-gateway
```

### Using the CLI Client

```bash
# Build the CLI tool
cd client-cli
mvn package

# Upload a file
java -jar target/client-cli-*-shaded.jar upload /path/to/file.txt --replication 3

# List files
java -jar target/client-cli-*-shaded.jar list --details

# Download a file
java -jar target/client-cli-*-shaded.jar download <file-id> --output downloaded-file.txt

# Get file information
java -jar target/client-cli-*-shaded.jar info <file-id>

# Delete a file
java -jar target/client-cli-*-shaded.jar delete <file-id>
```

### Using the REST API

```bash
# Upload a file
curl -X POST -F "file=@test.txt" -F "replicationFactor=3" http://localhost:8080/api/files/upload

# Download a file
curl -X GET http://localhost:8080/api/files/download/<file-id> --output downloaded.txt

# Get file metadata
curl -X GET http://localhost:8080/api/files/<file-id>/metadata

# List all files
curl -X GET http://localhost:8080/api/files

# Delete a file
curl -X DELETE http://localhost:8080/api/files/<file-id>
```

## Running Individual Services

### Metadata Service
```bash
cd metadata-service
mvn spring-boot:run -Dspring-boot.run.arguments="--ratis.node.id=metadata-1"
```

### Storage Node
```bash
cd storage-node
mvn spring-boot:run -Dspring-boot.run.arguments="--storage.node.id=storage-1"
```

### API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
cd test-harness
mvn test -Dgateway.url=http://localhost:8080
```

### Load Testing
```bash
# Run with Docker Compose environment
docker-compose up -d
cd test-harness
mvn test -Dtest=IntegrationTest#testSystemLoad
```

## Configuration

### Metadata Service Configuration
- `ratis.node.id`: Unique identifier for Raft node
- `ratis.nodes`: Comma-separated list of Raft cluster members
- `ratis.port`: Port for Raft communication

### Storage Node Configuration
- `storage.node.id`: Unique identifier for storage node
- `storage.port`: gRPC port for client communication
- `storage.data.directory`: Directory for storing file chunks

### API Gateway Configuration
- `metadata.endpoints`: Comma-separated list of metadata service endpoints
- `storage.endpoints`: Comma-separated list of storage node endpoints

## Monitoring and Operations

### Health Checks
- API Gateway: `GET /actuator/health`
- Individual services provide health endpoints

### Metrics
- System provides basic metrics for throughput and latency
- Integration with monitoring systems via standard interfaces

## Development

### Project Structure
```
├── common/                 # Shared utilities and DTOs
├── metadata-service/       # Raft-based metadata service
├── storage-node/          # gRPC storage nodes with JGroups
├── api-gateway/           # REST API gateway
├── client-cli/            # Command-line client
├── test-harness/          # Integration tests
├── docker-compose.yml     # Multi-node deployment
└── README.md
```

### Adding New Features
1. Define new DTOs in the `common` module
2. Implement service logic in appropriate modules
3. Add REST endpoints in `api-gateway`
4. Update CLI commands in `client-cli`
5. Add tests in `test-harness`

## Academic Purpose

This project demonstrates several key distributed systems concepts:

1. **CAP Theorem**: Shows trade-offs between consistency, availability, and partition tolerance
2. **Consensus Algorithms**: Implements Raft consensus for metadata consistency
3. **Replication Strategies**: Demonstrates different replication approaches
4. **Failure Detection**: Implements heartbeat-based failure detection
5. **Time Synchronization**: Shows logical and vector clock implementations
6. **Load Balancing**: Distributes load across multiple storage nodes

## License

This project is for academic purposes as part of SE2062 Distributed Systems coursework.
