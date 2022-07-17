package cn.qingweico.event;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

/**
 * @author zqw
 * @date 2022/1/24
 */
public class ObserverExample {
    public static void main(String[] args) {

        // 事件发送者
        EventObservable observable = new EventObservable();
        // 添加观察者(监听者)
        observable.addObserver(new EventObserver());
        // 发布消息(事件)
        observable.notifyObservers("this is a message!");

    }

    static class EventObservable extends Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }

        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(new EventObject(arg));
            clearChanged();
        }
    }

    static class EventObserver implements Observer, EventListener {

        @Override
        public void update(Observable o, Object arg) {
            EventObject eventObject = (EventObject) arg;
            System.out.println(eventObject.getSource());
        }
    }
}
