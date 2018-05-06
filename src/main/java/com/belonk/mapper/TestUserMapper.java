package com.belonk.mapper;

import com.belonk.domain.TestUser;

import java.util.List;

public interface TestUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TestUser record);

    int insertSelective(TestUser record);

    TestUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TestUser record);

    int updateByPrimaryKey(TestUser record);

    List<TestUser> selectAll();
}