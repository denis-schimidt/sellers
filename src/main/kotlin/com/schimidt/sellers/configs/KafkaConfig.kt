//package com.schimidt.sellers.configs
//
//import com.schimidt.sellers.controllers.SellerPersonalRequest
//import org.apache.kafka.clients.consumer.ConsumerConfig.*
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
//import org.springframework.kafka.core.ConsumerFactory
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory
//import org.springframework.kafka.support.serializer.JsonDeserializer
//
//@Configuration
//class KafkaConfig {
//
//    @Autowired
//    private lateinit var kafkaProperties: KafkaProperties
//
//    private fun consumerFactory(): ConsumerFactory<String, SellerPersonalRequest> {
//        val configs = kafkaProperties.properties.apply {
//            put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
//            put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer::class.java.name)
//            put(BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092, kafka2:9093, kafka3:9094")
//            put(GROUP_ID_CONFIG, "sellers-group")
//            put(AUTO_OFFSET_RESET_CONFIG, "earliest")
//        }.toMap()
//
//        return DefaultKafkaConsumerFactory(configs, StringDeserializer(), JsonDeserializer<SellerPersonalRequest>())
//    }
//
////    @Bean
////    fun producerFactory(): ProducerFactory<String, SellerPersonalRequest> {
////        val configs = kafkaProperties.properties.apply {
////            put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
////            put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer::class.java.name)
////            put(BOOTSTRAP_SERVERS_CONFIG, "kafka1:9092, kafka2:9093, kafka3:9094")
////        }.toMap()
////
////        return DefaultKafkaProducerFactory(configs, StringSerializer(), JsonSerializer<SellerPersonalRequest>())
////    }
////
////    @Bean
////    fun kafkaTemplate(): KafkaTemplate<String, Any>  {
////        return KafkaTemplate<String, Any>(producerFactory())
////    }
//
//    @Bean(name = ["fooKafkaListenerContainerFactory"])
//    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, SellerPersonalRequest>? {
//        val factory: ConcurrentKafkaListenerContainerFactory<String, SellerPersonalRequest> = ConcurrentKafkaListenerContainerFactory()
//        factory.consumerFactory = consumerFactory()
//        return factory
//    }
//}
