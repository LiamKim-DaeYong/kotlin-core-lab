package lab.functional

/**
 * 함수형 프로그래밍 - Result 패턴
 * 
 * 핵심 개념:
 * 1. Result<T> - 성공/실패를 타입으로 표현
 * 2. flatMap - 연쇄 처리에서 에러 전파
 * 3. fold - 성공/실패 케이스 처리
 * 4. 실무 적용 - 출고 취소 로직 개선
 */

// Result 확장함수들
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return when {
        isSuccess -> transform(getOrThrow())
        else -> Result.failure(exceptionOrNull()!!)
    }
}

// 물류 시스템 도메인 모델
data class Outbound(val id: Long, val status: OutboundStatus, val items: List<Item>)
data class Item(val productId: Long, val quantity: Int, val locationId: Long)

enum class OutboundStatus {
    SCHEDULED, ALLOCATED, PICKING, PICK_COMPLETE, SHIPPED
}

// 이벤트 정의
sealed class Event {
    data class OutboundCancelled(val outboundId: Long) : Event()
    data class InventoryTransferCancelled(val outboundId: Long) : Event()
    data class RestoreInstructionCreated(val outboundId: Long, val items: List<Item>) : Event()
}

fun main() {
    println("=== Result 패턴 실습 ===\n")
    
    // 1. 기본 Result 사용법
    basicResultExample()
    println()
    
    // 2. 연쇄 처리 (flatMap)
    chainedProcessingExample()
    println()
    
    // 3. 실무 적용: 출고 취소 로직
    outboundCancelExample()
}

fun basicResultExample() {
    println("1. 기본 Result 사용법")
    
    // 성공 케이스
    val successResult = validateOutboundId(123L)
    successResult.fold(
        onSuccess = { println("검증 성공: $it") },
        onFailure = { println("검증 실패: ${it.message}") }
    )
    
    // 실패 케이스
    val failureResult = validateOutboundId(-1L)
    failureResult.fold(
        onSuccess = { println("검증 성공: $it") },
        onFailure = { println("검증 실패: ${it.message}") }
    )
}

fun chainedProcessingExample() {
    println("2. 연쇄 처리 (flatMap)")
    
    val outboundId = 123L
    
    // 기존 방식: 각 단계마다 에러 체크
    val traditionalResult = processTraditional(outboundId)
    println("기존 방식 결과: $traditionalResult")
    
    // Result 패턴: 자동 에러 전파
    val functionalResult = processFunctional(outboundId)
    functionalResult.fold(
        onSuccess = { events -> println("함수형 방식 성공: ${events.size}개 이벤트 생성") },
        onFailure = { error -> println("함수형 방식 실패: $error") }
    )
}

fun outboundCancelExample() {
    println("3. 실무 적용: 출고 취소 로직")
    
    val testCases = listOf(
        Outbound(1L, OutboundStatus.SCHEDULED, emptyList()),
        Outbound(2L, OutboundStatus.ALLOCATED, listOf(Item(100L, 5, 1L))),
        Outbound(3L, OutboundStatus.PICK_COMPLETE, listOf(Item(200L, 3, 2L))),
        Outbound(4L, OutboundStatus.SHIPPED, emptyList())
    )
    
    testCases.forEach { outbound ->
        val result = cancelOutbound(outbound)
        result.fold(
            onSuccess = { events -> 
                println("출고 ${outbound.id} (${outbound.status}) 취소 성공: ${events.size}개 이벤트")
                events.forEach { println("  - $it") }
            },
            onFailure = { error -> 
                println("출고 ${outbound.id} (${outbound.status}) 취소 실패: $error")
            }
        )
        println()
    }
}

// 기본 검증 함수들
fun validateOutboundId(id: Long): Result<Long> {
    return if (id > 0) {
        Result.success(id)
    } else {
        Result.failure(IllegalArgumentException("출고 ID는 양수여야 합니다"))
    }
}

fun findOutbound(id: Long): Result<Outbound> {
    // 실제로는 DB 조회
    return if (id == 123L) {
        Result.success(Outbound(id, OutboundStatus.ALLOCATED, listOf(Item(100L, 5, 1L))))
    } else {
        Result.failure(Exception("출고를 찾을 수 없습니다"))
    }
}

fun validateCancelable(outbound: Outbound): Result<Outbound> {
    return if (outbound.status != OutboundStatus.SHIPPED) {
        Result.success(outbound)
    } else {
        Result.failure(Exception("이미 출고완료된 건은 취소할 수 없습니다"))
    }
}

// 기존 방식 vs 함수형 방식 비교
fun processTraditional(outboundId: Long): String {
    try {
        if (outboundId <= 0) return "ID 검증 실패"
        
        val outbound = findOutbound(outboundId).getOrNull() ?: return "출고 조회 실패"
        
        if (outbound.status == OutboundStatus.SHIPPED) return "취소 불가 상태"
        
        return "처리 성공"
    } catch (e: Exception) {
        return "예외 발생: ${e.message}"
    }
}

fun processFunctional(outboundId: Long): Result<List<Event>> {
    return validateOutboundId(outboundId)
        .flatMap { findOutbound(it) }
        .flatMap { validateCancelable(it) }
        .map { outbound ->
            listOf(Event.OutboundCancelled(outbound.id))
        }
}

// 실무 적용: 출고 취소 로직 (현재 sealed class 방식을 Result로 개선)
fun cancelOutbound(outbound: Outbound): Result<List<Event>> {
    return when (outbound.status) {
        OutboundStatus.SCHEDULED -> {
            // 출고 예정: 출고만 취소
            Result.success(listOf(Event.OutboundCancelled(outbound.id)))
        }
        
        OutboundStatus.ALLOCATED -> {
            // 할당됨: 출고 + 재고이동 취소
            Result.success(listOf(
                Event.OutboundCancelled(outbound.id),
                Event.InventoryTransferCancelled(outbound.id)
            ))
        }
        
        OutboundStatus.PICKING -> {
            // 피킹중: 출고만 취소 (작업 진행중)
            Result.success(listOf(Event.OutboundCancelled(outbound.id)))
        }
        
        OutboundStatus.PICK_COMPLETE -> {
            // 피킹완료: 출고 취소 + 원위치 지시서 생성
            Result.success(listOf(
                Event.OutboundCancelled(outbound.id),
                Event.RestoreInstructionCreated(outbound.id, outbound.items)
            ))
        }
        
        OutboundStatus.SHIPPED -> {
            // 출고완료: 취소 불가
            Result.failure(Exception("출고완료 상태에서는 취소할 수 없습니다"))
        }
    }
}