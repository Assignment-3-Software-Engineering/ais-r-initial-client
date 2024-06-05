package org.aisr.aisrinitialclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.aisr.aisrinitialclient.model.GlobalData;
import org.aisr.aisrinitialclient.model.UserSession;
import org.aisr.aisrinitialclient.model.data.Recruit;
import org.aisr.aisrinitialclient.model.dto.RecruitDto;
import org.aisr.aisrinitialclient.model.dto.StaffDto;
import org.aisr.aisrinitialclient.util.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RecruitService implements Closeable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Gson gson;

    public RecruitService() {
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            in = new ObjectInputStream( socket.getInputStream());
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        }catch(IOException e){
            e.printStackTrace();
        }
    }



    public Optional<Recruit> findById(String id) {
        System.out.println( "Find recruit by id: "+id);
        for (Recruit recruit : GlobalData.getInstance().getRecruitList()) {
            if (recruit.getId() == Integer.parseInt(id)) {
                return Optional.of(recruit);
            }
        }
        return Optional.empty();
    }

    public RecruitDto save(RecruitDto recruit) throws IOException {
        System.out.println("Save recruits data id: "+recruit.getId());
        recruit.setCreatedBy(GlobalData.getInstance().getSession().getLoggdinStaff());
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.CREATE_RECRUIT);
            out.writeObject(gson.toJson(recruit));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBody =  in.readObject();
                return gson.fromJson(responseBody.toString(), RecruitDto.class);
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

    public Recruit update(int id, Recruit recruit) throws IOException {
        System.out.println("Update recruits data id: "+id);
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.UPDATE_RECRUIT);
            out.writeObject(gson.toJson(recruit));
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBodyUser =  in.readObject();
                recruit = gson.fromJson(responseBodyUser.toString(), Recruit.class);

                GlobalData.getInstance().setSession( new UserSession(
                        GlobalData.getInstance().getSession().getLoggdinStaff(),
                        recruit,
                        GlobalData.getInstance().getSession().getLoggdinUser()));
            }else if (responseStatus.equals(ServerResponse.NOT_FOUND)){
                Object errorMessage =  in.readObject();
                throw new RuntimeException(errorMessage.toString());

            }else if (responseStatus.equals(ServerResponse.SERVER_ERROR)){
                Object errorMessage =  in.readObject();
                System.out.println(errorMessage);
                throw new RuntimeException(String.valueOf(errorMessage));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            close();
        }
        return recruit;
    }

    public List<Recruit> loadData() throws IOException, ClassNotFoundException {
        System.out.println("request recruits data from server ");
        try {
            socket = new Socket(ServerConnectionInfo.DEFAULT_HOST, ServerConnectionInfo.DEFAULT_PORT);
            out =new ObjectOutputStream(socket.getOutputStream()); // Set up object streams for communication over the socket
            in = new ObjectInputStream( socket.getInputStream());

            out.writeObject(ServerRequests.GET_ALL_RECRUIT);
            out.flush();

            Object responseStatus = in.readObject();
            if (responseStatus.equals(ServerResponse.OK)){
                Object responseBody =  in.readObject();
                Type recruitListType = new TypeToken<List<Recruit>>() {}.getType();
                List<Recruit> list = gson.fromJson(responseBody.toString(), recruitListType);
                GlobalData.getInstance().getRecruitList().clear();
                GlobalData.getInstance().getRecruitList().addAll(list);
                return list;
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
