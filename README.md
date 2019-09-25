# ClipLayout

#### 介绍
Anroid剪裁蒙版布局

#### 截图
![输入图片说明](https://images.gitee.com/uploads/images/2019/0925/103647_8ff51c08_134008.png "深度截图_选择区域_20190925103630.png")

#### 目前提供了ClipShapeFrameLayout，ClipConstraintLayout两种布局。
示例用法：

```
<cc.core.ui.cliplayout.ClipShapeFrameLayout
            android:id="@+id/csfl"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:clip_mask_color="@color/colorPrimary"
            app:layout_constraintBottom_toBottomOf="@id/iv1"
            app:layout_constraintEnd_toEndOf="@id/iv1"
            app:layout_constraintStart_toStartOf="@id/iv1"
            app:layout_constraintTop_toTopOf="@id/iv1">

            <View
                android:layout_width="140dp"
                android:layout_height="100dp"
                android:layout_gravity="center"
                app:layout_shape_radius="15dp" />

            <View
                android:layout_width="50dp"
                android:layout_height="50dp" />

            <View
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:layout_gravity="right"
                android:layout_margin="10dp"
                app:layout_shape_radius="100dp" />

        </cc.core.ui.cliplayout.ClipShapeFrameLayout>
```
自定义属性：

```
<declare-styleable name="ClipLayout">
        <!--        裁剪的圆角半径(此属性仅对子view有效)-->
        <attr name="layout_shape_radius" format="dimension" />
        <!--        是否忽略此子view的裁剪区域(此属性仅对子view有效)-->
        <attr name="layout_ignore_clip" format="boolean" />
        <!--        蒙版的颜色(此属性仅对父布局有效)-->
        <attr name="clip_mask_color" format="color" />
        <!--        是否停用裁剪（设为true将不做任何裁剪，和正常的布局一样使用，此属性仅对父布局有效）-->
        <attr name="ignore_all_clip" format="boolean" />
    </declare-styleable>
```
注意：
子view如果设置为gone，那么它的区域也将不会裁剪。
