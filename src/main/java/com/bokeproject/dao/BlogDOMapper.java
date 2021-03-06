package com.bokeproject.dao;

import com.bokeproject.dataobject.BlogDO;

import java.util.List;

public interface BlogDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int deleteByPrimaryKey(Integer blogid);
    List<BlogDO>listblog();
    List<BlogDO>listuserblog(Integer id);
    List<BlogDO>searchBlog(String shuru);
    List<BlogDO>listUserSave(List<Integer> BlogIdList);
    List<BlogDO>listUserZan(List<Integer> BlogIdList);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int insert(BlogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int insertSelective(BlogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    BlogDO selectByPrimaryKey(Integer blogid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int updateByPrimaryKeySelective(BlogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int updateByPrimaryKeyWithBLOBs(BlogDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table blog
     *
     * @mbg.generated Mon Apr 15 21:30:59 CST 2019
     */
    int updateByPrimaryKey(BlogDO record);
}