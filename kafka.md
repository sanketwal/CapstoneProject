The first step is to generate a new ID for your cluster


2.13-3.0.0
1
~/kafka_2.13-3.0.0/bin/kafka-storage.sh random-uuid
This returns a UUID, for example 76BLQI7sT_ql1mBfKsOk9Q

Next, format your storage directory (replace <uuid> by your UUID obtained above)


2.13-3.0.0
1
~/kafka_2.13-3.0.0/bin/kafka-storage.sh format -t <uuid> -c ~/kafka_2.13-3.0.0/config/kraft/server.properties
This will format the directory that is in the log.dirs in the config/kraft/server.properties file (by default /tmp/kraft-combined-logs)

Now you can launch the broker itself in daemon mode by running this command.


2.13-3.0.0
1
~/kafka_2.13-3.0.0/bin/kafka-server-start.sh ~/kafka_2.13-3.0.0/config/kraft