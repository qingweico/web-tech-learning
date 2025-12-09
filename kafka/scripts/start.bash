# 1. 启动 SpringBoot 应用
docker-compose up -d springboot-app

# 2. 健康检查
curl http://localhost:8081/actuator/health

# 3. 生成测试数据
curl -X POST "http://localhost:8082/api/kafka/generate-test-data?count=5"

# 4. 发送用户事件
curl -X POST "http://localhost:8082/api/kafka/user-event?username=testuser&action=REGISTER"

# 5. 发送订单事件
curl -X POST "http://localhost:8082/api/kafka/order-event?userId=user123&productId=prod456&quantity=2&amount=299.99&status=CREATED"

# 6. 查看 Kafka UI 中的消息
# 访问 http://localhost:8080
