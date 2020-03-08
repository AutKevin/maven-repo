# maven-repo

aeolian工具合集

### pom.xml

先引用仓库

```xml
<!--引用仓库-->
<repositories>
    <repository>
        <id>maven-repo-master</id>
        <url>https://raw.github.com/AutKevin/maven-repo/master/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

添加依赖

```xml
<dependency>
    <groupId>com.autumn</groupId>
    <artifactId>aeo-tool</artifactId>
    <version>1.0.0</version>
</dependency>
```
