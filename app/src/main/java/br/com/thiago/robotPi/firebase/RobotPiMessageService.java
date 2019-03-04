package br.com.thiago.robotPi.firebase;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import br.com.thiago.robotPi.dto.RaspberrySync;
import br.com.thiago.robotPi.event.AtualizaChatEvent;
import br.com.thiago.robotPi.event.AtualizaRaspberriesEvent;
import br.com.thiago.robotPi.model.Mensagem;

public class RobotPiMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Map<String, String> mensagem = remoteMessage.getData();
        Log.i("mensagem recebida", String.valueOf(mensagem));
        converteParaMensagem(mensagem);
    }

    private void converteParaMensagem(Map<String, String> mensagem) {
        String chaveDeAcessoRaspberrySync = "raspberrySync";
        String chaveDeAcessoMensagemComando = "mensagem";

        if(mensagem.containsKey(chaveDeAcessoRaspberrySync)){

            String json = mensagem.get(chaveDeAcessoRaspberrySync);
            ObjectMapper mapper = new ObjectMapper();
            try {
                RaspberrySync raspberrySync = mapper.readValue(json, RaspberrySync.class);
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new AtualizaRaspberriesEvent(raspberrySync));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(mensagem.containsKey(chaveDeAcessoMensagemComando)) {

            String json = mensagem.get(chaveDeAcessoMensagemComando);
            ObjectMapper mapper = new ObjectMapper();
            try {
                Mensagem mensagemToUser = mapper.readValue(json, Mensagem.class);
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new AtualizaChatEvent(mensagemToUser));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
