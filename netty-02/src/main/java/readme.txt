TCP黏包/拆包:
    TCP是一个“流”协议，所谓流，就是没有界限的一长串二进制数据。
    TCP作为传输层协议并不不了解上层业务数据的具体含义，它会根据TCP缓冲区的实际情况进行数据包的划分，
    所以在业务上认为是一个完整的包，可能会被TCP拆分成多个包进行发送，
    也有可能把多个小的包封装成一个大的数据包发送，这就是所谓的TCP粘包和拆包问题。

粘包问题的解决策略:

    1. 消息定长，报文大小固定长度，例如每个报文的长度固定为200字节，如果不够空位补空格；
    2. 包尾添加特殊分隔符，例如每条报文结束都添加回车换行符（例如FTP协议）或者指定特殊字符作为报文分隔符，接收方通过特殊分隔符切分报文区分；
    3. 将消息分为消息头和消息体，消息头中包含表示信息的总长度（或者消息体长度）的字段；
    4. 更复杂的自定义应用层协议。

 Netty粘包和拆包解决方案

    Netty提供了多个解码器，可以进行分包的操作，分别是：
    * LineBasedFrameDecoder 回车换行解码器
    * DelimiterBasedFrameDecoder（添加特殊分隔符报文来分包）
    * FixedLengthFrameDecoder（使用定长的报文来分包）
    * LengthFieldBasedFrameDecoder

LineBasedFrameDecoder解码器
    LineBasedFrameDecoder是回车换行解码器，如果用户发送的消息以回车换行符作为消息结束的标识，
    则可以直接使用Netty的LineBasedFrameDecoder对消息进行解码，
    只需要在初始化Netty服务端或者客户端时将LineBasedFrameDecoder正确的添加到ChannelPipeline中即可，
    不需要自己重新实现一套换行解码器。
