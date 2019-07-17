# Mini Phoenix ORM

[![N|Solid](http://images.vietid.net/avatars/1552103503273avatar-172075747.png)](http://images.vietid.net/avatars/1552103503273avatar-172075747.png)

**This library provides ORM-like mappings for Apache Hbase - Phonenix.<br>**
**I hope this small trick may help your work less boring :) <br>**
A Special Thanks To **Mr.PhuongHA** for Dynamic Query library
### Technologies
fully based on Java SE
* [Java Reflection] - for Java object mining 

### Installation
Install the repositories and dependencies.
add the following repository and dependency to your POM.xml file
```
    <repositories>
        <repository>
            <id>TrieuHiep</id>
            <name>mini-phoenix-orm</name>
            <url>https://github.com/TrieuHiep/mini-phoenix-orm/raw/master/</url>
        </repository>
    </repositories>
```
```
        <dependency>
            <groupId>com.nthzz.tatsuya</groupId>
            <artifactId>mini-phoenix-orm</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.apache.phoenix</groupId>
            <artifactId>phoenix-core</artifactId>
            <version>4.13.1-HBase-1.3</version>
            <exclusions>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>servlet-api-2.5</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>jetty</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>jetty-sslengine</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>jetty-util</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>jsp-2.1</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.mortbay.jetty</groupId>
                    <artifactId>jsp-api-2.1</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>javax.servlet</groupId>
                    <artifactId>servlet-api</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        
```
### Create Phoenix table
```
CREATE TABLE IF NOT EXISTS USER_AVATAR_HISTORY (
USER_ID BIGINT NOT NULL,
ADDED_TIMESTAMP TIMESTAMP NOT NULL,
AVATAR_CONTENT VARCHAR(255) DEFAULT NULL,
CONSTRAINT pk PRIMARY KEY (USER_ID, ADDED_TIMESTAMP DESC)
)SALT_BUCKETS=4, COMPRESSION='SNAPPY', DISABLE_WAL=true;
```
### create file <b>conf.properties</b>, add following property:

```$xslt
phoenix.data.source.url=your_phoenix_datasource_url
```

### Create Entity Class
create entity class and define table name, column name 

```java
import com.nthzz.tatsuya.persistence.Column;
import com.nthzz.tatsuya.persistence.Table;
import java.sql.Timestamp;
@Table(
    name = "USER_AVATAR_HISTORY"
)
public class AvatarHistory {
    @Column(
        name = "USER_ID"
    )
    private Long userID;
    @Column(
        name = "ADDED_TIMESTAMP"
    )
    private Timestamp addedTimestamp;
    @Column(
        name = "AVATAR_CONTENT"
    )
    private String avatarContent;

    public AvatarHistory() {
    }

    public AvatarHistory(Long userID, Timestamp addedTimestamp, String avatarContent) {
        this.userID = userID;
        this.addedTimestamp = addedTimestamp;
        this.avatarContent = avatarContent;
    }

    public Long getUserID() {
        return this.userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Timestamp getAddedTimestamp() {
        return this.addedTimestamp;
    }

    public void setAddedTimestamp(Timestamp addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }

    public String getAvatarContent() {
        return this.avatarContent;
    }

    public void setAvatarContent(String avatarContent) {
        this.avatarContent = avatarContent;
    }
}

```
#### create DAO layer and its implementation
DAO interface for AvatarHistory entity
```java
/**
 * @author tatsuya
 */
public interface HistoryAvatarDAO extends CrudRepository<AvatarHistory> {

}
```
and its implementation

```
public class AvatarHistoryDAOImpl extends AbstractFacade<AvatarHistory> implements HistoryAvatarDAO {
    public AvatarHistoryDAOImpl() {
        super(AvatarHistory.class);
    }
}
```

### using API
first, wiring DAO with its implemetation
```java
    HistoryAvatarDAO historyAvatarDAO = new AvatarHistoryDAOImpl();
```
* findAll - find all entity in table
```java
    List<AvatarHistory> avatarHistoryList =  historyAvatarDAO.findAll();
```
* count
```java
    long count = historyAvatarDAO.count();
```
* persist - create new entity, save to database
```java
        AvatarHistory avatarHistory = new AvatarHistory();
        avatarHistory.setUserID(1L);
        avatarHistory.setAvatarContent("my awesome avatar");
        avatarHistory.setAddedTimestamp(new Timestamp(System.currentTimeMillis()));
        historyAvatarDAO.persist(avatarHistory);
```
* persistBatch - save entities to database by batch size
```java
    List<AvatarHistory> avatarHistories = new ArrayList<>();
            for (long i = 0; i < 100; i++) {
                AvatarHistory avatarHistory = new AvatarHistory();
                avatarHistory.setUserID(i);
                avatarHistory.setAvatarContent("my awesome avatar");
                avatarHistory.setAddedTimestamp(new Timestamp(System.currentTimeMillis()));
                avatarHistories.add(avatarHistory);
            }
    
            historyAvatarDAO.persistBatch(avatarHistories, 50);
```
#### Finding by condition
create Dynamic Query
```java
        import com.nthzz.tatsuya.sql.DynamicQuery;
        import com.nthzz.tatsuya.sql.builder.QueryBuilders;
        import com.nthzz.tatsuya.sql.operator.conds.Equal;
        import com.nthzz.tatsuya.sql.operator.others.And;
        DynamicQuery dynamicQuery = QueryBuilders.create()
                .select()
                .column("*")
                .table(AvatarHistory.class)
                .where(
                        new And(new Equal("user_id", 69696969L)),
                        new And(new Equal("time_stamp", 637834657346578L))
                )
                .orderBy("added_timestamp", "DESC")
                .limit(10)
                .build();
```
the SQL command generated by calling 
```java 
dynamicQuery.getQuery()
```
is 
```
SELECT 
	*
FROM KINGHUB_USER_AVATAR_HISTORY
WHERE 1 = 1
	AND user_id = ? 
	AND time_stamp = ? 
ORDER BY added_timestamp DESC
LIMIT ? 
```
you can use this query by calling findByConditions function:

```java
    List<AvatarHistory> avatarHistories = historyAvatarDAO.findByConditions(dynamicQuery);
```

**TATSUYA-2019**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
