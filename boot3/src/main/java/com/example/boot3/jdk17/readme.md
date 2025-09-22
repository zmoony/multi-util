# Java 17
Java语言的最新版本，于2021年9月14日发布。以下是Java 17中的一些新特性：

1. Sealed类和接口

Java 17引入了一个新的语言特性，称为“Sealed类和接口”，用于限制类或接口的子类。使用Sealed类或接口可以限制子类的数量，并确保子类符合定义的限制条件。Sealed类或接口可以使用sealed关键字声明，并使用permits关键字指定子类。

2. Switch表达式增强

Java 17增强了switch表达式的功能，现在支持用于switch表达式的lambda表达式或方法引用。此外，switch表达式现在还支持使用arrow关键字(->)作为分支语句的分隔符。

3. Records类

Java 14中引入了Records类，Java 17进一步改进了Records类的功能。Records类是一种不可变的数据对象，它可以使用类似于声明属性的方式定义对象。它自动提供了一些默认方法，例如equals()和hashCode()方法。

4. 垃圾回收器改进

Java 17中包含了几项垃圾回收器的改进，包括了Epsilon垃圾回收器和ZGC垃圾回收器的改进。Epsilon垃圾回收器现在可以在Java 17中正式使用，并且现在支持在静态初始化期间使用。ZGC垃圾回收器现在可以在macOS上使用，而在Linux和Windows上仍然是实验性的。

5. 数字特性改进

Java 17中增加了一些数字特性，例如BigDecimal的toXXXExact()方法，可以将BigDecimal对象转换为其他数据类型，而不会出现损失。此外，Java 17中还增加了Math类中的新方法，例如Math.fma()方法，用于执行三个操作数的精确乘法和加法运算。

这些是Java 17中的一些主要特性，除此之外，Java 17中还包含了其他一些改进，例如Unicode 13支持、可共享类数据、G1垃圾回收器的并行完全垃圾回收等。