package me.main__.maincraft.world;

import java.util.LinkedList;
import java.util.List;

import me.main__.maincraft.entity.MainPlayer;

/*
 * I'm not sure if this is the best way to do it...
 */
public class ClientView {
    private final MainPlayer myself;

    public ClientView(MainPlayer self) {
        this.myself = self;
    }

    private List<ChunkCoords> preChunkedChunks = new LinkedList<ChunkCoords>();
    private List<ChunkCoords> sentChunks = new LinkedList<ChunkCoords>();

    private List<MainPlayer> visiblePlayers = new LinkedList<MainPlayer>();

    // ///////////////
    // Chunk stuff //
    // ///////////////
    public boolean isChunkPreChunked(ChunkCoords chunk) {
        return preChunkedChunks.contains(chunk);
    }

    public void preChunkedChunk(ChunkCoords chunk) {
        synchronized (preChunkedChunks) {
            preChunkedChunks.add(chunk);
        }
    }

    public boolean wasChunkSent(ChunkCoords chunk) {
        return sentChunks.contains(chunk);
    }

    public void sentChunk(ChunkCoords chunk) {
        synchronized (sentChunks) {
            sentChunks.add(chunk);
        }
    }

    public void unloadedChunk(ChunkCoords chunk) {
        synchronized (preChunkedChunks) {
            preChunkedChunks.remove(chunk);
        }
        synchronized (sentChunks) {
            sentChunks.remove(chunk);
        }
    }

    public List<ChunkCoords> getChunksToUnload(List<ChunkCoords> newChunks) {
        List<ChunkCoords> ret = new LinkedList<ChunkCoords>(preChunkedChunks);
        for (ChunkCoords cc : newChunks) {
            ret.remove(cc);
        }
        return ret;
    }

    // ////////////////
    // Player stuff //
    // ////////////////
    public boolean isPlayerVisible(MainPlayer player) {
        return visiblePlayers.contains(player);
    }

    public void showingPlayer(MainPlayer player) {
        if (player == myself)
            return;

        synchronized (visiblePlayers) {
            visiblePlayers.add(player);
        }
    }

    public void hidingPlayer(MainPlayer player) {
        if (player == myself)
            return;

        synchronized (visiblePlayers) {
            visiblePlayers.remove(player);
        }
    }

    public List<MainPlayer> getPlayersToHide(List<MainPlayer> newPlayers) {
        List<MainPlayer> ret = new LinkedList<MainPlayer>(visiblePlayers);
        ret.remove(myself);
        for (MainPlayer player : newPlayers) {
            ret.remove(player);
        }
        return ret;
    }
}
