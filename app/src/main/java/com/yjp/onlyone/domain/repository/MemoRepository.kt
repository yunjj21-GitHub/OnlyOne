package com.yjp.onlyone.domain.repository

interface MemoRepository {
    suspend fun getMemoContent(): String
    suspend fun saveMemoContent(content: String)
}
