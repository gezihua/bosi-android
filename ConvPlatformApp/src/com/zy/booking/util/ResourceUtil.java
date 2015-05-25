package com.zy.booking.util;


import java.lang.reflect.Field;

import com.zy.booking.R;

public class ResourceUtil {
   
   public static int getStringId(String fc) {
       int myimageId = R.string.app_name;
       try {
           if (fc != null) {
               try {
                   Field field_up = R.string.class.getField(fc);
                   myimageId = field_up.getInt(new R.string());
               } catch (Exception e) {
                   System.out.println(e);
               }
           }
       } catch (Exception e) {
       }
       return myimageId;
   }

}
