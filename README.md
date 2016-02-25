# JTA (Java Transaction Api)

> [JTA](http://baike.baidu.com/link?url=q4MNIj4OMWJqT552Ozf6Yy8plXTF3flKozyRJ3UISklpVeWSDOoDz3UgNHMp-c4UibJaPHaxZ8Jcu-JVCgwAh_)允许应用程序执行分布式事务处理——在两个或多个网络计算机资源上访问并且更新数据。


## Implementations

-   [Bitronix](https://github.com/bitronix/btm)
-   [Atomikos TransactionsEssentials](http://www.atomikos.com/Main/TransactionsEssentials)
-   [JOTM](http://jotm.ow2.org/)
-   [Simple JTA](http://simplejta.sourceforge.net/)

## Examples requirements

-   set MySQL **username** and **password** were 'root' 
-   create databases **test** and **test2**
-   execute *[init.sql](src/test/resources/init.sql)* in both databases.
-   Last, run command **gradle test**