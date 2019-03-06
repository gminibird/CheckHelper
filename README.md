# CheckHelper

[![](https://jitpack.io/v/gminibird/CheckHelper.svg)](https://jitpack.io/#gminibird/CheckHelper)

## 一 瞎扯淡
> [你还在为列表[单选]、[多选]写重复的逻辑吗](https://juejin.im/post/5c4d8325f265da616b110e9f)

项目中经常性会碰到列表的单选、多选，实现起来好像也不难，但是最近项目有好多个需要单选/多选的页面，看到设计稿的一瞬间，脑子灵光一闪，为啥不把这些简单而又繁琐的逻辑给封装起来呢(懒癌发作)？

于是就有了下面的小东西（开源库）...
## 二 功能
#### 1.功能
- 普通单选
- 普通多选
- 预选中
- 拦截器 since v1.1
- ...
#### 3.优点
- 简单易用
- 低耦合
- 不用为Bean添加额外字段
- 没有调用 `Adapter.notifyItemChange `方法，所以不会有闪屏Bug
## 三 使用
#### 1.配置
Step 1. 在你项目的 Project (根目录)的 build.gradle 文件添加下面配置：

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. 在需要使用的模块下添加以下依赖
```
	dependencies {
	        //Tag更换成最新的版本号，比如 1.1
	        implementation 'com.github.gminibird:CheckHelper:Tag'
	}
```
依赖包中依赖了`RecyclerView`，如果项目中已经使用了不同版本，可以使用以下配置：
```
	dependencies {
	        //Tag更换成最新的版本号，比如 1.1
	        implementation 'com.github.gminibird:CheckHelper:Tag',{
	            exclude group: 'com.android.support'
	        }
	}
```
最后那个1.0是版本号，可以上 [GitHub](https://github.com/gminibird/CheckHelper) 上看最新的，然后就可以愉快的玩耍啦。

#### 2.使用
- 创建CheckHelper实例
    ```
    SingleCheckHelper mCheckHelper = new SingleCheckHelper();
    //or
    MultiCheckHelper mCheckHelper = new MultiCheckHelper();
    ```
- 注册选择器
    ```
    mCheckHelper.register(String.class, new CheckHelper.Checker<String, LwViewHolder>()
    @Override
    public void check(String s, LwViewHolder holder) {
        //选中状态
        holder.itemView.setBackgroundColor(0xFF73E0E4); //蓝色
        holder.setChecked(R.id.checkbox, true);
    }
    @Override
    public void unCheck(String s, LwViewHolder holder) {
        //非选中状态
        holder.itemView.setBackgroundColor(0xFFFFFFFF);  //白色
        holder.setChecked(R.id.checkbox, false);
    }
    });
    ```
- 绑定到Adapter中
    ```
    @Override
    protected void onBind(@NonNull LwViewHolder holder, @NonNull String item) {
        //这里用了自己封装的Adapter，相当于onBindViewHolder方法
        mCheckHelper.bind(item, holder, holder.itemView);
    }
    ```
然后，然后就完成了。。。运行就可以看到想要的效果，选中的数据可以调用相应`CheckHelper`实例的`getXXX()`获取。


## 四 效果
废话少说，先上图：
#### 1.左到右分别为 单选->多选->拦截器
<center>
    <img src="https://user-gold-cdn.xitu.io/2019/1/27/1688e64e15aa79dc?w=374&h=607&f=gif&s=1047346" width ="240">
    <img src="https://user-gold-cdn.xitu.io/2019/1/27/1688e65eea8cb4ed?w=374&h=607&f=gif&s=1119303"
    width ="240">
    <img src="https://user-gold-cdn.xitu.io/2019/2/25/169235cacfe71475?w=368&h=598&f=gif&s=1349327"
    width ="240">
</center>

## 五 Api
#### 1. CheckHelper (基类)
| 返回值  | 方法                                         | 说明                                                         |
| :-----: | :------------------------------------------ | :----------------------------------------------------------- |
|  void   | register(Class, Checker)                    | 注册Checker，提供选中以及非选中状态                          |
|  void   | bind(Object, ViewHolder)                    | 绑定，没有指定点击View，需要额外设置点击事件，并主动调用select()方法 |
|  void   | bind(Object,ViewHolder, @IdRes int)         | 绑定，并指定触发事件的 View id                               |
|  void   | bind(Object,ViewHolder, View)               | 绑定，并指定触发事件的 View                                  |
|  void   | bind(Object, ViewHolder, boolean)           | 手动绑定                                                     |
|  void   | select(Object, ViewHolder)                  | 手动选择(状态值非当前状态的相反值)                           |
|  void   | select(Object, ViewHolder, boolean)         | 手动选择(可设置状态)                                         |
|  void   | addOnCheckListener(Class,OnCheckListene)    | 添加check监听(触发条件为bind监听与select监听的并集)          |
|  void   | addOnSelectListener(Class,OnSelectListener) | 添加select监听(点击时调用)                                   |
|  void   | addOnBindListener(Class,onBindListener)     | 添加bind监听(onBindViewHolder调用时调用)                     |
|  void   | addInterceptor(Interceptor interceptor)     | 添加拦截器                                                   |
| boolean | isChecked(Object,ViewHolder)                | 判断当前实例是否选中                                         |
| boolean | hasChecked()                                | 判断是否至少选中一个                                         |
|  void   | add(Object)                                 | 添加默认选中                                                 |
|  void   | remove(Object)                              | 删除选中                                                     |
|    T    | getChecked()                                | 获取所有选中                                                 |
#### 2. SingleCheckHelper（单选）
| 返回值 | 方法                   | 说明             |
| :----: | ---------------------- | ---------------- |
|  void  | setCanCancel(booleanl) | 设置是否可以取消 |
### 3. MultiCheckHelper（多选）
| 返回值  | 方法                                           | 说明                               |
| :-----: | ---------------------------------------------- | ---------------------------------- |
| Set<T>  | getChecked(Class<T>)                           | 获取指定类型的所有选中             |
| boolean | isAllChecked(List<?>)                          | 指定列表是否全部选中               |
| boolean | isAllChecked(List<?>, Class<T>)                | 指定列表里面的指定类型是否全部选中 |
|  void   | checkAll(Map<?,ViewHolder>)                    | 选择全部(指定数据)                 |
|  void   | checkAll(List<?>, Adapter)                     | 选择全部(指定数据)                 |
|  void   | unCheckAll(Adapter adapter)                    | 取消全部选中                       |
|  void   | unCheckAll(Adapter adapter, Class<T>, List<T>) | 取消全部选中(指定数据)             |
|  void   | unCheckAll(Adapter, Class)                     | 取消全部选中(指定数据，指定类型)   |


<br>
<br>

> written by [gminibird](https://github.com/gminibird/CheckHelper)

源码戳上面 ^^^
