package com.distributed.filestorage.storage.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.57.2)",
    comments = "Source: storage.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StorageServiceGrpc {

  private StorageServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "storage.StorageService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.StoreChunkRequest,
      com.distributed.filestorage.storage.grpc.StoreChunkResponse> getStoreChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StoreChunk",
      requestType = com.distributed.filestorage.storage.grpc.StoreChunkRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.StoreChunkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.StoreChunkRequest,
      com.distributed.filestorage.storage.grpc.StoreChunkResponse> getStoreChunkMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.StoreChunkRequest, com.distributed.filestorage.storage.grpc.StoreChunkResponse> getStoreChunkMethod;
    if ((getStoreChunkMethod = StorageServiceGrpc.getStoreChunkMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getStoreChunkMethod = StorageServiceGrpc.getStoreChunkMethod) == null) {
          StorageServiceGrpc.getStoreChunkMethod = getStoreChunkMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.StoreChunkRequest, com.distributed.filestorage.storage.grpc.StoreChunkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StoreChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.StoreChunkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.StoreChunkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("StoreChunk"))
              .build();
        }
      }
    }
    return getStoreChunkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.RetrieveChunkRequest,
      com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> getRetrieveChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RetrieveChunk",
      requestType = com.distributed.filestorage.storage.grpc.RetrieveChunkRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.RetrieveChunkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.RetrieveChunkRequest,
      com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> getRetrieveChunkMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.RetrieveChunkRequest, com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> getRetrieveChunkMethod;
    if ((getRetrieveChunkMethod = StorageServiceGrpc.getRetrieveChunkMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getRetrieveChunkMethod = StorageServiceGrpc.getRetrieveChunkMethod) == null) {
          StorageServiceGrpc.getRetrieveChunkMethod = getRetrieveChunkMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.RetrieveChunkRequest, com.distributed.filestorage.storage.grpc.RetrieveChunkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RetrieveChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.RetrieveChunkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.RetrieveChunkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("RetrieveChunk"))
              .build();
        }
      }
    }
    return getRetrieveChunkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.DeleteChunkRequest,
      com.distributed.filestorage.storage.grpc.DeleteChunkResponse> getDeleteChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteChunk",
      requestType = com.distributed.filestorage.storage.grpc.DeleteChunkRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.DeleteChunkResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.DeleteChunkRequest,
      com.distributed.filestorage.storage.grpc.DeleteChunkResponse> getDeleteChunkMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.DeleteChunkRequest, com.distributed.filestorage.storage.grpc.DeleteChunkResponse> getDeleteChunkMethod;
    if ((getDeleteChunkMethod = StorageServiceGrpc.getDeleteChunkMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getDeleteChunkMethod = StorageServiceGrpc.getDeleteChunkMethod) == null) {
          StorageServiceGrpc.getDeleteChunkMethod = getDeleteChunkMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.DeleteChunkRequest, com.distributed.filestorage.storage.grpc.DeleteChunkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.DeleteChunkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.DeleteChunkResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("DeleteChunk"))
              .build();
        }
      }
    }
    return getDeleteChunkMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ListChunksRequest,
      com.distributed.filestorage.storage.grpc.ListChunksResponse> getListChunksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListChunks",
      requestType = com.distributed.filestorage.storage.grpc.ListChunksRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.ListChunksResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ListChunksRequest,
      com.distributed.filestorage.storage.grpc.ListChunksResponse> getListChunksMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ListChunksRequest, com.distributed.filestorage.storage.grpc.ListChunksResponse> getListChunksMethod;
    if ((getListChunksMethod = StorageServiceGrpc.getListChunksMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getListChunksMethod = StorageServiceGrpc.getListChunksMethod) == null) {
          StorageServiceGrpc.getListChunksMethod = getListChunksMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.ListChunksRequest, com.distributed.filestorage.storage.grpc.ListChunksResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListChunks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.ListChunksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.ListChunksResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("ListChunks"))
              .build();
        }
      }
    }
    return getListChunksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.HealthCheckRequest,
      com.distributed.filestorage.storage.grpc.HealthCheckResponse> getHealthCheckMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "HealthCheck",
      requestType = com.distributed.filestorage.storage.grpc.HealthCheckRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.HealthCheckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.HealthCheckRequest,
      com.distributed.filestorage.storage.grpc.HealthCheckResponse> getHealthCheckMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.HealthCheckRequest, com.distributed.filestorage.storage.grpc.HealthCheckResponse> getHealthCheckMethod;
    if ((getHealthCheckMethod = StorageServiceGrpc.getHealthCheckMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getHealthCheckMethod = StorageServiceGrpc.getHealthCheckMethod) == null) {
          StorageServiceGrpc.getHealthCheckMethod = getHealthCheckMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.HealthCheckRequest, com.distributed.filestorage.storage.grpc.HealthCheckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "HealthCheck"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.HealthCheckRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.HealthCheckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("HealthCheck"))
              .build();
        }
      }
    }
    return getHealthCheckMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ReplicationRequest,
      com.distributed.filestorage.storage.grpc.ReplicationResponse> getReplicateChunkMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReplicateChunk",
      requestType = com.distributed.filestorage.storage.grpc.ReplicationRequest.class,
      responseType = com.distributed.filestorage.storage.grpc.ReplicationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ReplicationRequest,
      com.distributed.filestorage.storage.grpc.ReplicationResponse> getReplicateChunkMethod() {
    io.grpc.MethodDescriptor<com.distributed.filestorage.storage.grpc.ReplicationRequest, com.distributed.filestorage.storage.grpc.ReplicationResponse> getReplicateChunkMethod;
    if ((getReplicateChunkMethod = StorageServiceGrpc.getReplicateChunkMethod) == null) {
      synchronized (StorageServiceGrpc.class) {
        if ((getReplicateChunkMethod = StorageServiceGrpc.getReplicateChunkMethod) == null) {
          StorageServiceGrpc.getReplicateChunkMethod = getReplicateChunkMethod =
              io.grpc.MethodDescriptor.<com.distributed.filestorage.storage.grpc.ReplicationRequest, com.distributed.filestorage.storage.grpc.ReplicationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReplicateChunk"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.ReplicationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.distributed.filestorage.storage.grpc.ReplicationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StorageServiceMethodDescriptorSupplier("ReplicateChunk"))
              .build();
        }
      }
    }
    return getReplicateChunkMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StorageServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StorageServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StorageServiceStub>() {
        @java.lang.Override
        public StorageServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StorageServiceStub(channel, callOptions);
        }
      };
    return StorageServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StorageServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StorageServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StorageServiceBlockingStub>() {
        @java.lang.Override
        public StorageServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StorageServiceBlockingStub(channel, callOptions);
        }
      };
    return StorageServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StorageServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StorageServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StorageServiceFutureStub>() {
        @java.lang.Override
        public StorageServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StorageServiceFutureStub(channel, callOptions);
        }
      };
    return StorageServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Store a file chunk
     * </pre>
     */
    default void storeChunk(com.distributed.filestorage.storage.grpc.StoreChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.StoreChunkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStoreChunkMethod(), responseObserver);
    }

    /**
     * <pre>
     * Retrieve a file chunk
     * </pre>
     */
    default void retrieveChunk(com.distributed.filestorage.storage.grpc.RetrieveChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRetrieveChunkMethod(), responseObserver);
    }

    /**
     * <pre>
     * Delete a file chunk
     * </pre>
     */
    default void deleteChunk(com.distributed.filestorage.storage.grpc.DeleteChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.DeleteChunkResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteChunkMethod(), responseObserver);
    }

    /**
     * <pre>
     * List stored chunks
     * </pre>
     */
    default void listChunks(com.distributed.filestorage.storage.grpc.ListChunksRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ListChunksResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListChunksMethod(), responseObserver);
    }

    /**
     * <pre>
     * Health check
     * </pre>
     */
    default void healthCheck(com.distributed.filestorage.storage.grpc.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHealthCheckMethod(), responseObserver);
    }

    /**
     * <pre>
     * Replicate chunk to other nodes
     * </pre>
     */
    default void replicateChunk(com.distributed.filestorage.storage.grpc.ReplicationRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ReplicationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReplicateChunkMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StorageService.
   */
  public static abstract class StorageServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StorageServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StorageService.
   */
  public static final class StorageServiceStub
      extends io.grpc.stub.AbstractAsyncStub<StorageServiceStub> {
    private StorageServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StorageServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StorageServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a file chunk
     * </pre>
     */
    public void storeChunk(com.distributed.filestorage.storage.grpc.StoreChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.StoreChunkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStoreChunkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Retrieve a file chunk
     * </pre>
     */
    public void retrieveChunk(com.distributed.filestorage.storage.grpc.RetrieveChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRetrieveChunkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Delete a file chunk
     * </pre>
     */
    public void deleteChunk(com.distributed.filestorage.storage.grpc.DeleteChunkRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.DeleteChunkResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteChunkMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * List stored chunks
     * </pre>
     */
    public void listChunks(com.distributed.filestorage.storage.grpc.ListChunksRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ListChunksResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListChunksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Health check
     * </pre>
     */
    public void healthCheck(com.distributed.filestorage.storage.grpc.HealthCheckRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.HealthCheckResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Replicate chunk to other nodes
     * </pre>
     */
    public void replicateChunk(com.distributed.filestorage.storage.grpc.ReplicationRequest request,
        io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ReplicationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReplicateChunkMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StorageService.
   */
  public static final class StorageServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StorageServiceBlockingStub> {
    private StorageServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StorageServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StorageServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a file chunk
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.StoreChunkResponse storeChunk(com.distributed.filestorage.storage.grpc.StoreChunkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStoreChunkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Retrieve a file chunk
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.RetrieveChunkResponse retrieveChunk(com.distributed.filestorage.storage.grpc.RetrieveChunkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRetrieveChunkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Delete a file chunk
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.DeleteChunkResponse deleteChunk(com.distributed.filestorage.storage.grpc.DeleteChunkRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteChunkMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * List stored chunks
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.ListChunksResponse listChunks(com.distributed.filestorage.storage.grpc.ListChunksRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListChunksMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Health check
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.HealthCheckResponse healthCheck(com.distributed.filestorage.storage.grpc.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHealthCheckMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Replicate chunk to other nodes
     * </pre>
     */
    public com.distributed.filestorage.storage.grpc.ReplicationResponse replicateChunk(com.distributed.filestorage.storage.grpc.ReplicationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReplicateChunkMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StorageService.
   */
  public static final class StorageServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<StorageServiceFutureStub> {
    private StorageServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StorageServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StorageServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Store a file chunk
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.StoreChunkResponse> storeChunk(
        com.distributed.filestorage.storage.grpc.StoreChunkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStoreChunkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Retrieve a file chunk
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.RetrieveChunkResponse> retrieveChunk(
        com.distributed.filestorage.storage.grpc.RetrieveChunkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRetrieveChunkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Delete a file chunk
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.DeleteChunkResponse> deleteChunk(
        com.distributed.filestorage.storage.grpc.DeleteChunkRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteChunkMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * List stored chunks
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.ListChunksResponse> listChunks(
        com.distributed.filestorage.storage.grpc.ListChunksRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListChunksMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Health check
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.HealthCheckResponse> healthCheck(
        com.distributed.filestorage.storage.grpc.HealthCheckRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHealthCheckMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Replicate chunk to other nodes
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.distributed.filestorage.storage.grpc.ReplicationResponse> replicateChunk(
        com.distributed.filestorage.storage.grpc.ReplicationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReplicateChunkMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_STORE_CHUNK = 0;
  private static final int METHODID_RETRIEVE_CHUNK = 1;
  private static final int METHODID_DELETE_CHUNK = 2;
  private static final int METHODID_LIST_CHUNKS = 3;
  private static final int METHODID_HEALTH_CHECK = 4;
  private static final int METHODID_REPLICATE_CHUNK = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STORE_CHUNK:
          serviceImpl.storeChunk((com.distributed.filestorage.storage.grpc.StoreChunkRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.StoreChunkResponse>) responseObserver);
          break;
        case METHODID_RETRIEVE_CHUNK:
          serviceImpl.retrieveChunk((com.distributed.filestorage.storage.grpc.RetrieveChunkRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.RetrieveChunkResponse>) responseObserver);
          break;
        case METHODID_DELETE_CHUNK:
          serviceImpl.deleteChunk((com.distributed.filestorage.storage.grpc.DeleteChunkRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.DeleteChunkResponse>) responseObserver);
          break;
        case METHODID_LIST_CHUNKS:
          serviceImpl.listChunks((com.distributed.filestorage.storage.grpc.ListChunksRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ListChunksResponse>) responseObserver);
          break;
        case METHODID_HEALTH_CHECK:
          serviceImpl.healthCheck((com.distributed.filestorage.storage.grpc.HealthCheckRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.HealthCheckResponse>) responseObserver);
          break;
        case METHODID_REPLICATE_CHUNK:
          serviceImpl.replicateChunk((com.distributed.filestorage.storage.grpc.ReplicationRequest) request,
              (io.grpc.stub.StreamObserver<com.distributed.filestorage.storage.grpc.ReplicationResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getStoreChunkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.StoreChunkRequest,
              com.distributed.filestorage.storage.grpc.StoreChunkResponse>(
                service, METHODID_STORE_CHUNK)))
        .addMethod(
          getRetrieveChunkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.RetrieveChunkRequest,
              com.distributed.filestorage.storage.grpc.RetrieveChunkResponse>(
                service, METHODID_RETRIEVE_CHUNK)))
        .addMethod(
          getDeleteChunkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.DeleteChunkRequest,
              com.distributed.filestorage.storage.grpc.DeleteChunkResponse>(
                service, METHODID_DELETE_CHUNK)))
        .addMethod(
          getListChunksMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.ListChunksRequest,
              com.distributed.filestorage.storage.grpc.ListChunksResponse>(
                service, METHODID_LIST_CHUNKS)))
        .addMethod(
          getHealthCheckMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.HealthCheckRequest,
              com.distributed.filestorage.storage.grpc.HealthCheckResponse>(
                service, METHODID_HEALTH_CHECK)))
        .addMethod(
          getReplicateChunkMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.distributed.filestorage.storage.grpc.ReplicationRequest,
              com.distributed.filestorage.storage.grpc.ReplicationResponse>(
                service, METHODID_REPLICATE_CHUNK)))
        .build();
  }

  private static abstract class StorageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StorageServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.distributed.filestorage.storage.grpc.StorageProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StorageService");
    }
  }

  private static final class StorageServiceFileDescriptorSupplier
      extends StorageServiceBaseDescriptorSupplier {
    StorageServiceFileDescriptorSupplier() {}
  }

  private static final class StorageServiceMethodDescriptorSupplier
      extends StorageServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    StorageServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StorageServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StorageServiceFileDescriptorSupplier())
              .addMethod(getStoreChunkMethod())
              .addMethod(getRetrieveChunkMethod())
              .addMethod(getDeleteChunkMethod())
              .addMethod(getListChunksMethod())
              .addMethod(getHealthCheckMethod())
              .addMethod(getReplicateChunkMethod())
              .build();
        }
      }
    }
    return result;
  }
}
