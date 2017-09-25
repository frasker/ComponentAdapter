# ComponentAdapter
RecyclerView 插拔式Adapter
组件注册式API，将数据类型和View类型的绑定抽离出来  
<code>
  componentAdapter = new ComponentAdapter(this)  
                .registerItemComponent(new ComponentType0())  
                .registerItemComponent(new ComponentType1())  
                .setData(data);
</code>
具体使用参见demo
