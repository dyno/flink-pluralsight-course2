# https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/try-flink/flink-operations-playground/
# in operations-playground of https://github.com/apache/flink-playgrounds.git

SHELL = /bin/bash

KFK_TOPICS := docker-compose exec kafka kafka-topics.sh --bootstrap-server localhost:9092
KFK_PRODUCER := docker-compose exec kafka kafka-console-producer.sh --broker-list localhost:9092
KFK_CONSUMER := docker-compose exec kafka kafka-console-consumer.sh --bootstrap-server localhost:9092
KFK_CLIENT := docker-compose run --no-deps client

TOPIC := ipl-news

kafka-topics-create:
	$(KFK_TOPICS) --create --topic $(TOPIC) --partitions 1 --replication-factor 1

kafka-topics-list:
	$(KFK_TOPICS) --list

kafka-producer:
	$(KFK_PRODUCER) --topic $(TOPIC)

kafka-consumer:
	$(KFK_CONSUMER) --topic $(TOPIC)

kafka-consumer2:
	$(KFK_CONSUMER) --topic $(TOPIC) --from-beginning

kafka-client:
	cp $(PROJECT_BASE)/build/libs/flink-pluralsight-course-1.0.0-all.jar conf/
	$(KFK_CLIENT) flink run /opt/flink/conf/flink-pluralsight-course-1.0.0-all.jar
	$(KFK_CLIENT) flink list


