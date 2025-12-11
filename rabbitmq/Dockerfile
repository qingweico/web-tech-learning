FROM rabbitmq:3.13-management

# 先下载 x-delayed-message 插件到本地(plugins目录下)
#  curl -L -o rabbitmq_delayed_message_exchange-3.13.0.ez \
#  https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.13.0/rabbitmq_delayed_message_exchange-3.13.0.ez
COPY plugins/rabbitmq_delayed_message_exchange-3.13.0.ez /opt/rabbitmq/plugins/
# 启用插件
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange
