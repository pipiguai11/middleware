package com.lhw.apply.service;

import org.springframework.stereotype.Service;

/**
 * @author ：linhw
 * @date ：22.3.16 16:59
 * @description：
 *
 *      @CacheEvict是用来标注在需要清除缓存元素的方法或类上的。
 *      当标记在一个类上时表示其中所有的方法的执行都会触发缓存的清除操作。
 *
 *      @CacheEvict可以指定的属性有value、key、condition、allEntries和beforeInvocation。
 *
 *      其中value、key和condition的语义与@Cacheable对应的属性类似。
 *      value表示清除操作是发生在哪些Cache上的（对应Cache的名称）；
 *      key表示需要清除的是哪个key，如未指定则会使用默认策略生成的key；
 *      condition表示清除操作发生的条件
 *
 *      allEntries是boolean类型，表示是否需要清除缓存中的所有元素。
 *          默认为false，表示不需要。当指定了allEntries为true时，Spring Cache将忽略指定的key。
 *          有的时候我们需要Cache一下清除所有的元素，这比一个一个清除元素更有效率。
 *      beforeInvocation表示清除操作默认是在对应方法成功执行之后触发的，即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。
 *          使用beforeInvocation可以改变触发清除操作的时间，当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素
 *
 * @modified By：
 */
@Service
public class CacheEvictTestImpl {
}
