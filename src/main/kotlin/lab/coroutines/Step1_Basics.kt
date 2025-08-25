package lab.coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 코루틴 기초 - 기본 개념 정리
 * 
 * 핵심 개념:
 * 1. suspend 함수 - 일시 중단 가능한 함수
 * 2. launch vs async - fire-and-forget vs 결과 반환
 * 3. runBlocking - 코루틴과 일반 코드 연결
 * 4. 구조화된 동시성 (Structured Concurrency)
 */

// 기본 suspend 함수들
suspend fun fetchUserData(userId: Long): String {
    delay(1000) // 네트워크 호출 시뮬레이션
    return "User-$userId"
}

suspend fun fetchUserOrders(userId: Long): List<String> {
    delay(800) // DB 조회 시뮬레이션
    return listOf("Order-1", "Order-2", "Order-3")
}

suspend fun processOrder(order: String): String {
    delay(500) // 처리 시간 시뮬레이션
    return "Processed: $order"
}

fun main(): Unit = runBlocking {
    println("=== 코루틴 기초 실습 ===\n")
    
    // 1. 순차 실행 vs 병렬 실행 비교
    sequentialExecution()
    println()
    parallelExecution()
    println()
    
    // 2. launch vs async 차이점
    launchVsAsync()
    println()
    
    // 3. 구조화된 동시성
    structuredConcurrency()
}

suspend fun sequentialExecution() {
    println("1. 순차 실행")
    val time = measureTimeMillis {
        val userData = fetchUserData(123L)
        val orders = fetchUserOrders(123L)
        println("순차 결과: $userData, 주문 수: ${orders.size}")
    }
    println("순차 실행 시간: ${time}ms")
}

suspend fun parallelExecution() {
    println("2. 병렬 실행")
    val time = measureTimeMillis {
        coroutineScope {
            val userDataDeferred = async { fetchUserData(123L) }
            val ordersDeferred = async { fetchUserOrders(123L) }
            
            val userData = userDataDeferred.await()
            val orders = ordersDeferred.await()
            println("병렬 결과: $userData, 주문 수: ${orders.size}")
        }
    }
    println("병렬 실행 시간: ${time}ms")
}

suspend fun launchVsAsync() {
    println("3. launch vs async")
    
    coroutineScope {
        // launch: 결과를 반환하지 않음 (fire-and-forget)
        val job = launch {
            delay(1000)
            println("launch: 백그라운드 작업 완료")
        }
        
        // async: 결과를 반환함
        val deferred = async {
            delay(500)
            "async: 계산 결과"
        }
        
        println("async 결과: ${deferred.await()}")
        job.join() // launch 완료 대기
    }
}

suspend fun structuredConcurrency() {
    println("4. 구조화된 동시성")
    
    try {
        coroutineScope { // 모든 자식 코루틴이 완료될 때까지 대기
            val jobs = listOf("Order-1", "Order-2", "Order-3").map { order ->
                async { processOrder(order) }
            }
            
            val results = jobs.awaitAll()
            println("모든 주문 처리 완료: $results")
        }
    } catch (e: Exception) {
        println("오류 발생: ${e.message}")
    }
}