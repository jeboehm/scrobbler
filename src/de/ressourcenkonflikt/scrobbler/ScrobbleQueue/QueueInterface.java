package de.ressourcenkonflikt.scrobbler.ScrobbleQueue;

/**
 * Created with IntelliJ IDEA.
 * User: jeff
 * Date: 13.08.13
 * Time: 07:26
 * To change this template use File | Settings | File Templates.
 */
public interface QueueInterface {
    /**
     * Pull the first song from the queue.
     */
    public Song shift();
}
