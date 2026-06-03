package com.yjp.onlyone.data.repository

import com.yjp.onlyone.data.local.dao.MemoDao
import com.yjp.onlyone.data.local.entity.MemoEntity
import com.yjp.onlyone.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao,
) : MemoRepository {

    override suspend fun getMemoContent(): String {
        return memoDao.getContent() ?: ""
    }

    override suspend fun saveMemoContent(content: String) {
        memoDao.upsert(
            MemoEntity(content = content),
        )
    }
}
