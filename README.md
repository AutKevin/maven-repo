# maven-repo

aeolian工具合集

### Maven仓库

 典型的一个maven依赖下会有这三个文件： 

```json
maven-metadata.xml 
maven-metadata.xml.md5 
maven-metadata.xml.sha1 
```

`maven-metadata.xml`里面记录了最后deploy的版本和时间。  md5、sha1校验文件是用来保证这个meta文件的完整性。 

maven在编绎项目时，会先尝试请求maven-metadata.xml，如果没有找到，则会直接尝试请求到jar文件，在下载jar文件时也会尝试下载jar的md5, sha1文件。

maven-metadata.xml文件很重要，如果没有这个文件来指明最新的jar版本，那么即使远程仓库里的jar更新了版本，本地maven编绎时用上-U参数，也不会拉取到最新的jar！

所以并不能简单地把jar包放到github上就完事了，一定要<u>**先在本地Deploy（target/mvn-repo下面），生成maven-metadata.xml文件，并上传到github**</u>上。

### pom.xml

先引用仓库

```xml
<!--引用github仓库(已经失效)-->
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

或者

```xml
<repository>
    <id>tomcata-repository</id>
    <url>http://52zt.info:88/mvn</url>
</repository>
```

添加依赖

```xml
<dependency>
    <groupId>com.autumn</groupId>
    <artifactId>aeo-tool</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 引用本地仓库

 想要使用本地file仓库里，在项目的pom.xml里配置。

```xml
<repository>
    <id>local-maven</id>
    <url>file:D:\ProgramFiles_QY\IdeaProjects\maven-repo\target\mvn-repo</url>
</repository>
```



