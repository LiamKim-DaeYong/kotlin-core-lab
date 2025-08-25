package lab.functional

import lab.coroutines.basicFlowExample

/**
 * 커스텀 Result 패턴 구현하기
 * 
 * 미션:
 * 1. MyResult<T> sealed class 만들기
 * 2. map, flatMap, fold 함수 구현하기
 * 3. 실무 예제에 적용해보기
 */

// TODO: 1단계 - MyResult sealed class 정의하기
// 힌트: Success와 Failure 케이스가 필요해요
// sealed class MyResult<out T> {
//     // Success 케이스: 값을 가지고 있음
//     // Failure 케이스: 에러를 가지고 있음
// }
sealed class MyResult<out T> {
    data class Success<T>(val value: T): MyResult<T>()
    data class Failure(val exception: Throwable): MyResult<Nothing>()

     companion object {
        fun <T> success(value: T): MyResult<T> {
            return Success(value)
        }
        fun <T> failure(error: Throwable): MyResult<T> {
            return Failure(error)
        }
    }
}

// TODO: 2단계 - 기본 함수들 구현하기
// 힌트: 
// - map: 성공 시에만 값을 변환
// - flatMap: 성공 시에만 다음 MyResult 반환 함수 실행
// - fold: 성공/실패 케이스를 각각 처리
fun <T, R> MyResult<T>.map(transform: (T) -> R): MyResult<R> {
    return when (this) {
        is MyResult.Success -> {
            val newValue = transform(this.value)
            MyResult.Success(newValue)
        }
        is MyResult.Failure -> {
            this
        }
    }
}

fun <T, R> MyResult<T>.flatMap(transform: (T) -> MyResult<R>): MyResult<R> {
    return when (this) {
        is MyResult.Success -> transform(this.value)
        is MyResult.Failure -> this
    }
}

fun <T, R> MyResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (Throwable) -> R,
): R {
    return when (this) {
        is MyResult.Success -> onSuccess(this.value)
        is MyResult.Failure -> onFailure(this.exception)
    }
}

// TODO: 3단계 - 편의 함수들 구현하기
// companion object {
//     fun <T> success(value: T): MyResult<T>
//     fun <T> failure(error: Throwable): MyResult<T>
// }

// 테스트용 도메인 모델 (간단하게)
data class User(val id: Long, val name: String, val email: String)

// 테스트 함수들
fun validateUserId(id: Long): MyResult<Long> {
    // TODO: 구현하기
    // 힌트: id > 0이면 성공, 아니면 실패
    return if (id > 0) {
        MyResult.success(id)
    } else {
        MyResult.failure(IllegalArgumentException("양수여야 합니다."))
    }
}

fun findUser(id: Long): MyResult<User> {
    // TODO: 구현하기  
    // 힌트: id == 1L이면 더미 유저 반환, 아니면 실패
    return if (id == 1L) {
        MyResult.success(User(1L, "Jake", "<EMAIL>"))
    } else {
        MyResult.failure(Exception("유저를 찾을 수 없습니다."))
    }
}

fun validateEmail(user: User): MyResult<User> {
    // TODO: 구현하기
    // 힌트: email에 "@"가 있으면 성공, 아니면 실패
    return if (user.email.contains("@")) {
        MyResult.success(user)
    } else {
        MyResult.failure(IllegalArgumentException("이메일 형식이 아닙니다."))
    }
}

fun main() {
    println("=== 커스텀 Result 패턴 구현 ===\n")
    
    // TODO: 테스트 케이스들 작성하기
    // 1. 기본 성공/실패 케이스
    // 2. map 테스트
    // 3. flatMap 체이닝 테스트
    // 4. fold 테스트
    validateUserId(1L)
        .flatMap { findUser(it) }
        .flatMap { validateEmail(it) }
        .fold(
            onSuccess = { user -> "성공: $user" },
            onFailure = { error -> "실패: ${error.message}" }
        )
    
    println("구현을 완료하면 여기서 테스트해보세요!")
}

/*
구현 가이드:

1단계 - sealed class 구조:
sealed class MyResult<out T> {
    data class Success<T>(val value: T) : MyResult<T>()
    data class Failure(val error: Throwable) : MyResult<Nothing>()
}

2단계 - 핵심 함수들:
- map: Success일 때만 변환 함수 적용
- flatMap: Success일 때만 다음 MyResult 함수 실행  
- fold: Success/Failure 각각 다른 함수 적용

3단계 - 실제 사용:
validateUserId(1L)
    .flatMap { findUser(it) }
    .flatMap { validateEmail(it) }
    .fold(
        onSuccess = { user -> "성공: $user" },
        onFailure = { error -> "실패: ${error.message}" }
    )

천천히 하나씩 구현해보세요!
*/