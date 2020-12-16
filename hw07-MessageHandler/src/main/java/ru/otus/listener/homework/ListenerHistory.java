package ru.otus.listener.homework;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class ListenerHistory implements Listener {


    private final Queue<HistoryMessage> historyMessage = new LinkedList();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyMessage.add(new HistoryMessage(oldMsg, newMsg));
    }

    public Queue<HistoryMessage> getHistoryMessage() {
        return Optional.ofNullable(historyMessage)
            .map(Queue::stream)
            .orElseGet(Stream::empty)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    public void printHistory() {
        historyMessage.forEach(el->System.out.println("OldMsg: "+el.getOldMsg().toString()+"\nnewMsg: "+el.newMsg.toString()+"\n"));
    }

    public static class HistoryMessage{

        private final Message oldMsg;
        private final Message newMsg;

        private HistoryMessage(Message oldMsg, Message newMsg) {
            this.oldMsg = oldMsg.copy();
            this.newMsg = newMsg.copy();
        }
        public Message getOldMsg() {
            return oldMsg;
        }

        public Message getNewMsg() {
            return newMsg;
        }
    }

}
