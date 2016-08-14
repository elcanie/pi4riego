package com.smartcity.pi4riego.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.smartcity.pi4riego.ApplicationStartup;
import com.smartcity.pi4riego.entity.DeviceI2C;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eliasibz on 14/08/16.
 */
public class DeviceI2CController {

    private static final int START_ADDRESS = 5;
    private static final int END_ADDRESS = 6;

    public static void write(DeviceI2C device, String action) throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        I2CDevice i2cDevice = i2c.getDevice(device.getAddressNumber());

        i2cDevice.write(action.getBytes());
    }

    public static ArrayList<Integer> discoverThings() throws IOException, I2CFactory.UnsupportedBusNumberException, ParseException {
        ArrayList<Integer> things = new ArrayList<Integer>();
        I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
        byte[] buffer = new byte[100];
        for(int i=START_ADDRESS;i<=END_ADDRESS;i++){
            //try{

                I2CDevice i2cDevice = i2c.getDevice(i);
                i2cDevice.write("{'res':'info'}".getBytes());


                i2cDevice.read(buffer, 0, 100);


            /*}catch(Exception e){
                ApplicationStartup.getConsole().println("En la direccion "+i+" no hay nada.");
            }*/

            String s = new String(buffer, "UTF-8");
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(s);
            ApplicationStartup.getConsole().println(json.keySet());
            things.add(i);
        }

        return things;
    }

}
