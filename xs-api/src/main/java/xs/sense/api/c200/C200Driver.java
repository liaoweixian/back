package xs.sense.api.c200;

import xs.sense.api.utils.ConstantSense;

/**
 * @author mikehhuang
 * @date 2020/3/23 21:08
 */
public class C200Driver {
    public static   C200Interface c200Interface  ;

    public static void main(String[] args) {

        init_board();

        alarm_on();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        alarm_off();


        red_on();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        red_off();


        yellow_on();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        yellow_off();

        green_on();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        green_off();


        close_board();
    }

    public static void getInstance() {
        if(null==c200Interface){
            c200Interface = C200Interface.INSTANCE;
        }
    }
    public static int init_board() {
        getInstance();
        int resp = c200Interface.iob_board_init(ConstantSense.comNo,null);
        System.out.println("resp = "+ resp);
        return resp;
    }



    public static int close_board() {
        getInstance();
        int resp = c200Interface.iob_board_close(ConstantSense.comNo,false);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int alarm_on() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,1,true);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int alarm_off() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,1,false);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int red_on() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,2,true);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int red_off() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,2,false);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int yellow_on() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,3,true);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int yellow_off() {
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,3,false);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int green_on() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,4,true);
        System.out.println("resp = "+ resp);
        return resp;
    }
    public static int green_off() {
        getInstance();
        int resp = c200Interface.iob_write_outbit(ConstantSense.comNo,4,false);
        System.out.println("resp = "+ resp);
        return resp;
    }


}
