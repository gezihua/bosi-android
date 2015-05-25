question 1： 在代码中设置的xml selector 怎么在代码中使用

两个viewPagger 的嵌套使用  如果调度滑动时间和 页面的绘制的位置 


3.通知栏上方如何 生产滚动的消息


4.工程可以直接用@引用子工程的资源

5.style 的item的使用注意技巧 。犯过一个错误 是关于 tabindicator 的不显示效果 因为 系统的style 未添加 其attr 的style的引用


6.注意listview 的getview 长时间执行的问题 ，一旦出现这个问题 首先查 其layoutParams 的height是否为0  若不为0 则检查 onMeasure 方法这是的宽高以及模式等


7.修改listview 中添加EditText的bug  点击Edit的时候 弹出软键盘  Edit 无法正常正常获取焦点 。 解决办法: 设置选中的EditText 手动获取 requestFouce (注意这个是关键点)

8.修改处理图片类 通过file 的fd 获取图片时 出现图片处理不全 只显示上半部分 或者 全部都是白的 就是当时联想督导出现的问题  注意MyThurmbBitmapUtil 的createBitmap 的相关处理逻辑


9。代码创建 ViewPagger 时


当在activity中使用viewpage时，切忌要手动设置它的id，
否则这样当setAdaper后会出现Resources$NotFoundException: Resource ID #0xffffffff的异常 要手动设置setIdid


10. 遇到一个对于listview 中的一项View 添加selector 不起作用的问题

    selector 添加在该项view的父布局上 一直不起作用 但是直接换到该view 上就可以 （特别注意在ListView的Item中使用selecotr的问题）
    
    
 11.
 keytool -genkey -alias android.keystore -keyalg RSA -validity 20000 -keystore android.keystore  生成keystore 文件 默认放在java目录