package com.zfy.light.sample;

import java.util.Random;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
public class Utils {

    static String[] IMAGES = {
            "http://cdn1.showjoy.com/shop/images/20181109/MJJOB5DIKY7AQI3TB7TL1541749374017.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/LT35SH74BTLP2QJ566UA1541749373747.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/B35W44T43SQEZ8TFCEQD1541749373720.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/JA7N67VWSNCFMHW6ABZT1541749373856.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/75ZAJL37SLM4FFJO2REA1541749373901.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/UKEFZ7BO2BQII87JTZ7Z1541749373760.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/4ZD4OD5ULX53VDDNSDLN1541749373493.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/POR699FZVWSXXJK7TY7T1541749373604.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/M7ZKZTGOSNL6NXF43IGZ1541749373520.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/I2ONMP1MS5JH6OVEWGA21541749373824.jpg",
            "http://cdn1.showjoy.com/shop/images/20181109/2EDXWXNGTT5KYBMMHX6K1541749364453.png",
    };


    public static String randomImage() {
        int index = new Random().nextInt(IMAGES.length - 1);
        return IMAGES[index];
    }
}
