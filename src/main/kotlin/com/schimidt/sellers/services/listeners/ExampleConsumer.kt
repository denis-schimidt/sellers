//package com.schimidt.sellers.services.listeners
//
//import com.schimidt.sellers.controllers.SellerPersonalRequest
//import org.slf4j.LoggerFactory
//import org.springframework.kafka.annotation.KafkaListener
//import org.springframework.stereotype.Component
//
//@Component
//class ExampleConsumer {
//    private val logger = LoggerFactory.getLogger(this.javaClass)
//
//    @KafkaListener(topics = ["sellers.created"], groupId = "sellers-group", containerFactory = "fooKafkaListenerContainerFactory")
//    fun firstListener(seller: SellerPersonalRequest) {
//        logger.info("Message received: [$seller]")
//    }
//}
