package lab.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Flow 기초 - 비동기 스트림 처리
 * 
 * 핵심 개념:
 * 1. Flow 생성 - flowOf, flow { }, asFlow()
 * 2. 중간 연산자 - map, filter, transform
 * 3. 종료 연산자 - collect, toList, reduce
 * 4. 실무 패턴 - 실시간 데이터 처리
 */

// 물류 시스템 예제 데이터
data class Order(val id: Long, val status: String, val amount: Int)
data class DeliveryUpdate(val orderId: Long, val location: String, val timestamp: Long)

fun main() = runBlocking {
    println("=== Flow 기초 실습 ===\n")
    
    // 1. Flow 기본 생성과 수집
    basicFlowExample()
    println()
    
    // 2. Flow 변환 연산자들
    flowTransformations()
    println()
    
    // 3. 실시간 주문 처리 시뮬레이션
    realTimeOrderProcessing()
    println()
    
    // 4. 배송 추적 시뮬레이션
    deliveryTracking()
}

suspend fun basicFlowExample() {
    println("1. Flow 기본 생성과 수집")
    
    // 간단한 Flow 생성
    val numbersFlow = flowOf(1, 2, 3, 4, 5)
    
    println("기본 Flow:")
    numbersFlow.collect { value ->
        println("수집된 값: $value")
    }
    
    // 비동기 Flow 생성
    val asyncFlow = flow {
        for (i in 1..3) {
            delay(500) // 비동기 작업 시뮬레이션
            emit("데이터-$i")
        }
    }
    
    println("\n비동기 Flow:")
    asyncFlow.collect { value ->
        println("비동기 수집: $value")
    }
}

suspend fun flowTransformations() {
    println("2. Flow 변환 연산자들")
    
    val orders = listOf(
        Order(1, "PENDING", 1000),
        Order(2, "PROCESSING", 2000),
        Order(3, "PENDING", 500),
        Order(4, "SHIPPED", 1500)
    ).asFlow()
    
    // map + filter 조합
    orders
        .filter { it.status == "PENDING" }
        .map { "주문 ${it.id}: ${it.amount}원 처리 대기" }
        .collect { println(it) }
    
    println()
    
    // transform으로 복잡한 변환
    orders
        .transform { order ->
            if (order.amount >= 1000) {
                emit("고액 주문: ${order.id}")
                emit("금액: ${order.amount}원")
            }
        }
        .collect { println(it) }
}

suspend fun realTimeOrderProcessing() {
    println("3. 실시간 주문 처리 시뮬레이션")
    
    // 실시간으로 들어오는 주문들을 시뮬레이션
    val incomingOrders = flow {
        val orders = listOf(
            Order(101, "NEW", 2500),
            Order(102, "NEW", 800),
            Order(103, "NEW", 3200),
            Order(104, "NEW", 1200)
        )
        
        for (order in orders) {
            delay(1000) // 1초마다 새 주문
            emit(order)
        }
    }
    
    // 주문 처리 파이프라인
    incomingOrders
        .onEach { println("새 주문 접수: ${it.id}") }
        .filter { it.amount >= 1000 } // 1000원 이상만 처리
        .map { order ->
            delay(500) // 처리 시간
            order.copy(status = "PROCESSED")
        }
        .collect { processedOrder ->
            println("처리 완료: 주문 ${processedOrder.id} (${processedOrder.amount}원)")
        }
}

suspend fun deliveryTracking() {
    println("4. 배송 추적 시뮬레이션")
    
    // 배송 업데이트 스트림
    val deliveryUpdates = flow {
        val updates = listOf(
            DeliveryUpdate(101, "물류센터", System.currentTimeMillis()),
            DeliveryUpdate(101, "배송중", System.currentTimeMillis() + 1000),
            DeliveryUpdate(101, "배송완료", System.currentTimeMillis() + 2000)
        )
        
        for (update in updates) {
            delay(800)
            emit(update)
        }
    }
    
    // 특정 주문의 배송 상태만 추적
    deliveryUpdates
        .filter { it.orderId == 101L }
        .map { "주문 ${it.orderId}: ${it.location} (${it.timestamp})" }
        .collect { println(it) }
}