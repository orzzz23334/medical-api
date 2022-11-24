## 定义关系到知识图谱的映射时的写法
```java
@RelationshipEntity(RelationConstant.EVALUATE)
@Getter
@Setter
@ToString(callSuper = true)
public class CompanyEnvAAEvaluate extends Evaluate {
    @StartNode
    @JsonIgnore
    @ToString.Exclude
    private Company company;
    @EndNode
    @JsonIgnore
    @ToString.Exclude
    private EnvAssessmentAnnouncement envAssessmentAnnouncement;

}
```

* 套娃

    ```java
    @JsonIgnore
    @ToString.Exclude
    ```
  写这些就是为了防止实体关系无线循环的出现:

    实体1{关系1：{实体1：{...}}}

* 为什么不用@Data

    因为@Data会重写HashCode方法，重写的时候同样会涉及套娃问题，从而导致栈溢出。

### 不熟悉的地方
* 一个已经向上转型的类（例如为object类）在序列化的时候如何做到直接序列化其真实类型中的所有属性的
* 序列化部分具体学习

## Service 中的问题

* option
  
  如果数据库中没有这个实体的话返回的不是空指针，而是一个空列表。

* position
  
  有几个node的方向整错了好像

## 序列化与反序列化

* 反序列化
  * 类中有的字段但是JSON中没有，对应的字段为null

# 动态关系建模

要求定义的节点基类为抽象类，不然关系的查询结果会为空

```java
// 超类 要求使用泛型的时候NodeAbstract是抽象类
@Data
public abstract class RelationAbstract<S extends NodeAbstract, E extends NodeAbstract> implements RelationTransferable {
    @Id
    @GeneratedValue
    public Long id;

    @StartNode
    @JsonIgnore // 由于父类是return this， this 实际是一个子类对象所以要写JsonIgnore
    @ToString.Exclude // 防止test的时候，调用toString引起的栈溢出问题
    public S startNode;
    @EndNode
    @JsonIgnore
    @ToString.Exclude
    public E endNode; // 泛型动态指定起始节点类型
}
// 子类
@Data
@Jacksonized
@RelationshipEntity(RelationConstant.CONSTRUCT)
public class Construct<S extends NodeAbstract, E extends NodeAbstract> extends RelationAbstract<S, E> {
  @Property
  private String contact;

  @JsonIgnore
  public Construct getConstruct(){
    return this;
  }

  @Override
  public RelationDto buildRelationDto() {
    return new RelationDto<>(RelationConstant.CONSTRUCT, startNode.getId(), endNode.getId(), this);
  }
}
```

## 同关系同方向 识别冲突
```java
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER, direction = Relationship.INCOMING)
    private List<StockholderRelation<Person, Company>> stockHolderPerson; // TODO:
    @JsonIgnore
    @ToString.Exclude
    @ApiModelProperty(hidden = true)
    @Relationship(type =  RelationConstant.STOCKHOLDER, direction = Relationship.INCOMING)
    private List<StockholderRelation<Company, Company>> stockHolderCompany;// TODO:
```

如上两个关系如果不能从关系类型和方向上将其区分开的话，运行时java也不能根据起始节点类型将其区分开

所以两个关系都映射不成功，都会是空值

采取的方案：

* 在relation dao 里面手动查询冲突的关系
```java
    // 命名规则： find [实体类中的关系名] By [实体类名]
    @Query("match (c:Company)<-[r:STOCKHOLDER]-(o:Person) where id(c) = $companyId return c,r,o")
    List<StockholderRelation<Person, Company>> findStockHolderPersonByCompany(@Param("companyId") Company company);
    @Query("match (c:Company)<-[r:STOCKHOLDER]-(o:Company) where id(c) = $companyId return c,r,o")
    List<StockholderRelation<Company, Company>> findStockHolderCompanyByCompany(@Param("companyId") Company company);
```



