package org.aisr.aisrinitialclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.data.Staff;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.util.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StaffService implements Closeable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Gson gson;


    public StaffService()  {
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            in = new ObjectInputStream( socket.getInputStream());
            gson =  new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Optional<Staff> findById(String id) {
/*        System.out.println("Find staff by id: "+id);
        for (Staff staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getId().equals(id)) {
                return Optional.of(staff);
            }
        }*/
        return Optional.empty();
    }

    public static Staff getStaffById(String id){
/*        for (Staff staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getId().equals(id)) {
                return staff;
            }
        }*/
        return null;
    }

    public Optional<Staff> findByEmail(String email) {
/*        System.out.println( "Find staff by email: "+email);
        for (Staff staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getEmail().equals(email)) {
                return Optional.of(staff);
            }
        }*/
        return Optional.empty();
    }

    public Optional<Staff> findByUsername(String username) {
/*        System.out.println("Find staff by username : "+username);
        for (Staff staff : GlobalData.getInstance().getStaffList()) {
            if (staff.getUsername().equals(username)) {
                return Optional.of(staff);
            }
        }*/
        return Optional.empty();
    }

    public StaffDto save(StaffDto staff) throws IOException {
        System.out.println("Save staff data id: "+staff.getId());
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.CREATE_STAFF);
            out.writeObject(gson.toJson(staff));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBody =  in.readObject();
                return gson.fromJson(responseBody.toString(),StaffDto.class);
            } else if (responseStatus.equals(ServerResponse.SERVER_ERROR)){
                Object errorMessage =  in.readObject();
                System.out.println(errorMessage);
                throw new RuntimeException(String.valueOf(errorMessage));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            close();
        }
        return null;
    }

    public List<StaffDto> loadData() throws IOException, ClassNotFoundException {
        System.out.println("request staff data from server ");
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.GET_ALL_STAFF);
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBody =  in.readObject();
                Type type = new TypeToken<List<StaffDto>>() {}.getType();
                List<StaffDto> list = gson.fromJson(responseBody.toString(), type);
                GlobalData.getInstance().getStaffList().clear();
                GlobalData.getInstance().getStaffList().addAll(list);
                return list;
            } else if (responseStatus.equals(ServerResponse.SERVER_ERROR)){
                Object errorMessage =  in.readObject();
                System.out.println(errorMessage);
                throw new RuntimeException(String.valueOf(errorMessage));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            close();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null && !socket.isClosed()) socket.close();
    }
}
