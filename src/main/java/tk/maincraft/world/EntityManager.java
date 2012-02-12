package tk.maincraft.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tk.maincraft.MainServer;
import tk.maincraft.Maincraft;
import tk.maincraft.entity.MainPlayer;
import tk.maincraft.packet.PacketClient;
import tk.maincraft.packet.PacketClientAction;
import tk.maincraft.packet.out.DestroyEntityPacket;
import tk.maincraft.packet.out.EntityLookAndRelMovePacket;
import tk.maincraft.packet.out.EntityLookPacket;
import tk.maincraft.packet.out.EntityRelMovePacket;
import tk.maincraft.packet.out.EntityTeleportPacket;
import tk.maincraft.packet.out.MapChunkPacket;
import tk.maincraft.packet.out.NamedEntitySpawnPacket;
import tk.maincraft.packet.out.PreChunkPacket;
import tk.maincraft.packet.out.impl.OutputDestroyEntityPacket;
import tk.maincraft.packet.out.impl.OutputEntityLookAndRelMovePacket;
import tk.maincraft.packet.out.impl.OutputEntityLookPacket;
import tk.maincraft.packet.out.impl.OutputEntityRelMovePacket;
import tk.maincraft.packet.out.impl.OutputEntityTeleportPacket;
import tk.maincraft.packet.out.impl.OutputMapChunkPacket;
import tk.maincraft.packet.out.impl.OutputNamedEntitySpawnPacket;
import tk.maincraft.packet.out.impl.OutputPreChunkPacket;
import tk.maincraft.util.RotationUtils;


public class EntityManager {

    private final Map<String, MainPlayer> playerList = new ConcurrentHashMap<String, MainPlayer>();
    private final MainServer server;
    private final MainWorld world;

    private int nextIntId = 0;

    public EntityManager(MainServer server, MainWorld world) {
        this.server = server;
        this.world = world;
    }

    public MainPlayer newPlayer(String playername, PacketClient client) {
        // TODO load some data
        Location spawnLoc = world.getSpawnLocation();
        Vector velocity = new Vector();
        int id = nextIntId++;
        UUID uid = UUID.randomUUID();
        MainPlayer newPlayer = new MainPlayer(spawnLoc, velocity, id, uid, server, playername,
                client);
        playerList.put(playername, newPlayer);

        return newPlayer;
    }

    private static final int VIEW_DISTANCE = Maincraft.getServer().getConfig().getIOSettings()
            .getViewDistance();

    private static enum EntityUpdateReason {
        CHANGE_POSITION,
        CHANGE_LOOK,
        CHANGE_LOOK_AND_POSITION,
    }

    /*
     * What's this!? -------------- Let me explain: If entities/players are moving less than 4 blocks, we can use a relative move-packet. That's much better. However, packets might get lost or clients may just fail to receive them. Then entities are at wrong places on the client. So we randomly decide to send a teleport-packet to correct that. -- main()
     */
    private static final int RANDOM_SIZE = Maincraft.getServer().getConfig().getIOSettings()
            .getEntityNoRelVal() + 1;

    public void sendPlayerUpdates(final MainPlayer player, final EntityUpdateReason reason) {
        if (!playerList.containsValue(player))
            throw new IllegalArgumentException("We don't know that this player exists!");

        int minChunkX = player.getLocation().getChunk().getX() - VIEW_DISTANCE;
        int minChunkZ = player.getLocation().getChunk().getZ() - VIEW_DISTANCE;
        int maxChunkX = player.getLocation().getChunk().getX() + VIEW_DISTANCE;
        int maxChunkZ = player.getLocation().getChunk().getZ() + VIEW_DISTANCE;

        final List<MainPlayer> requiredPlayers = new LinkedList<MainPlayer>();

        // now let's iterate through all players we're watching
        for (MainPlayer otherPlayer : playerList.values()) {
            if (otherPlayer == player)
                continue;

            int otherChunkX = otherPlayer.getLocation().getChunk().getX();
            int otherChunkZ = otherPlayer.getLocation().getChunk().getZ();
            if ((otherChunkX >= minChunkX) && (otherChunkX <= maxChunkX)
                    && (otherChunkZ >= minChunkZ) && (otherChunkZ <= maxChunkZ)) {
                // send that player to the client
                requiredPlayers.add(otherPlayer);
            }
        }

        final List<MainPlayer> playersToHide = player.getClientView().getPlayersToHide(
                requiredPlayers);

        player.getPacketClient().scheduleClientAction(new PacketClientAction<Void>() {
            public Void call(PacketClient client) throws Exception {
                for (MainPlayer otherPlayer : requiredPlayers) {
                    if (!player.getClientView().isPlayerVisible(otherPlayer)) {
                        // named entity spawn
                        int x = otherPlayer.getLocation().getBlockX() * 32;
                        int y = otherPlayer.getLocation().getBlockY() * 32;
                        int z = otherPlayer.getLocation().getBlockZ() * 32;
                        byte yaw = RotationUtils.floatToByte(otherPlayer.getLocation().getYaw());
                        byte pitch = RotationUtils
                                .floatToByte(otherPlayer.getLocation().getPitch());
                        short currentItem = 0; // TODO
                        NamedEntitySpawnPacket namedEntitySpawnPacket = new OutputNamedEntitySpawnPacket(
                                player.getPacketClient(), otherPlayer.getEntityId(), otherPlayer
                                        .getName(), x, y, z, yaw, pitch, currentItem);
                        namedEntitySpawnPacket.send();

                        player.getClientView().showingPlayer(otherPlayer);
                    }
                    // but wait: the other one might want to see us :D
                    if (!otherPlayer.getClientView().isPlayerVisible(player)) {
                        // named entity spawn
                        int x = player.getLocation().getBlockX() * 32;
                        int y = player.getLocation().getBlockY() * 32;
                        int z = player.getLocation().getBlockZ() * 32;
                        byte yaw = RotationUtils.floatToByte(player.getLocation().getYaw());
                        byte pitch = RotationUtils.floatToByte(player.getLocation().getPitch());
                        short currentItem = 0; // TODO
                        NamedEntitySpawnPacket namedEntitySpawnPacket = new OutputNamedEntitySpawnPacket(
                                otherPlayer.getPacketClient(), player.getEntityId(), player
                                        .getName(), x, y, z, yaw, pitch, currentItem);
                        namedEntitySpawnPacket.send();

                        otherPlayer.getClientView().showingPlayer(player);
                    }
                    else {
                        // he DOES know about us, but we still can send the changes to him
                        switch (reason) {
                        case CHANGE_LOOK: // changed pitch&yaw
                            byte yaw1 = RotationUtils.floatToByte(player.getLocation().getYaw());
                            byte pitch1 = RotationUtils
                                    .floatToByte(player.getLocation().getPitch());
                            EntityLookPacket lookPacket = new OutputEntityLookPacket(otherPlayer
                                    .getPacketClient(), player.getEntityId(), yaw1, pitch1);
                            lookPacket.send();
                            break;
                        case CHANGE_POSITION:
                            Location oldLoc1 = player.getOldLocation();
                            Location newLoc1 = player.getLocation();

                            double diffX1 = (newLoc1.getX() - oldLoc1.getX()) * 32;
                            double diffY1 = (newLoc1.getY() - oldLoc1.getY()) * 32;
                            double diffZ1 = (newLoc1.getZ() - oldLoc1.getZ()) * 32;

                            boolean randomSaysYes1 = MainServer.rand.nextInt(RANDOM_SIZE) != 0;
                            if (randomSaysYes1 && (Math.abs(diffX1) < 128)
                                    && (Math.abs(diffY1) < 128) && (Math.abs(diffZ1) < 128)) {
                                // it's okay, it fits in relMove
                                EntityRelMovePacket packet = new OutputEntityRelMovePacket(
                                        otherPlayer.getPacketClient(), player.getEntityId(),
                                        (byte) diffX1, (byte) diffY1, (byte) diffZ1);
                                packet.send();
                            }
                            else {
                                // too big difference, we need a teleport-packet
                                EntityTeleportPacket packet = new OutputEntityTeleportPacket(
                                        otherPlayer.getPacketClient(), player.getEntityId(),
                                        newLoc1);
                                packet.send();
                            }
                            break;
                        case CHANGE_LOOK_AND_POSITION: // we changed position
                            Location oldLoc2 = player.getOldLocation();
                            Location newLoc2 = player.getLocation();

                            double diffX2 = (newLoc2.getX() - oldLoc2.getX()) * 32;
                            double diffY2 = (newLoc2.getY() - oldLoc2.getY()) * 32;
                            double diffZ2 = (newLoc2.getZ() - oldLoc2.getZ()) * 32;
                            byte yaw2 = RotationUtils.floatToByte(newLoc2.getYaw());
                            byte pitch2 = RotationUtils.floatToByte(newLoc2.getPitch());

                            boolean randomSaysYes2 = MainServer.rand.nextInt(RANDOM_SIZE) != 0;
                            if (randomSaysYes2 && (Math.abs(diffX2) < 128)
                                    && (Math.abs(diffY2) < 128) && (Math.abs(diffZ2) < 128)) {
                                // it's okay, it fits in relMove
                                EntityLookAndRelMovePacket packet = new OutputEntityLookAndRelMovePacket(
                                        otherPlayer.getPacketClient(), player.getEntityId(),
                                        (byte) diffX2, (byte) diffY2, (byte) diffZ2, yaw2, pitch2);
                                packet.send();
                            }
                            else {
                                // too big difference, we need a teleport-packet
                                EntityTeleportPacket packet = new OutputEntityTeleportPacket(
                                        otherPlayer.getPacketClient(), player.getEntityId(),
                                        newLoc2);
                                packet.send();
                            }
                            break;
                        default: // *sigh* :P
                            throw new UnsupportedOperationException(
                                    "The programmer added a new value to the enum and forgot to handle it. *sigh*");
                        }
                    }
                }

                for (MainPlayer otherPlayer : playersToHide) {
                    DestroyEntityPacket destroyPacket = new OutputDestroyEntityPacket(client,
                            otherPlayer.getEntityId());
                    destroyPacket.send();
                }
                return null;
            }
        });
    }

    public void sendChunkUpdates(final MainPlayer player) {
        if (!playerList.containsValue(player))
            throw new IllegalArgumentException("We don't know that this player exists!");

        final List<ChunkCoords> requiredChunks = new LinkedList<ChunkCoords>();
        int middleX = player.getLocation().getChunk().getX();
        int middleZ = player.getLocation().getChunk().getZ();
        Bukkit.getLogger().finest(String.format("middleZ: %s; middleX: %s", middleZ, middleX));
        // Create a square of chunks around the player
        for (int x = (middleX - VIEW_DISTANCE); x < (middleX + VIEW_DISTANCE); x++) {
            for (int z = (middleZ - VIEW_DISTANCE); z < (middleZ + VIEW_DISTANCE); z++) {
                requiredChunks.add(new ChunkCoords(x, z));
                world.loadChunk(x, z);
            }
        }

        player.getPacketClient().scheduleClientAction(new PacketClientAction<Void>() {
            public Void call(PacketClient client) throws Exception {
                for (ChunkCoords cc : requiredChunks) {
                    // send pre-chunks, if necessary
                    MainChunk chunk = (MainChunk) world.getChunkAt(cc.x, cc.z);
                    if (!player.getClientView().isChunkPreChunked(cc)) {
                        PreChunkPacket packet = new OutputPreChunkPacket(client, cc.x, cc.z, true);
                        packet.send();

                        // now it is preChunked
                        player.getClientView().preChunkedChunk(cc);
                    }

                    if (!player.getClientView().wasChunkSent(cc)) {
                        byte[] chunkdata = chunk.serializeToByteArray();
                        MapChunkPacket packet = new OutputMapChunkPacket(client, cc.x
                                * MainChunk.SIZE_X, (short) 0, cc.z * MainChunk.SIZE_Z,
                                MainChunk.SIZE_X, MainChunk.HEIGHT, MainChunk.SIZE_Z, chunkdata);
                        Bukkit.getLogger().finest(
                                String.format("Constructed MapChunkPacket: %s", packet.toString()));
                        packet.send();

                        // now it was sent
                        player.getClientView().sentChunk(cc);
                    }
                }
                List<ChunkCoords> chunksToUnload = player.getClientView().getChunksToUnload(
                        requiredChunks);
                for (ChunkCoords cc : chunksToUnload) {
                    PreChunkPacket packet = new OutputPreChunkPacket(client, cc.x, cc.z, false);
                    packet.send();

                    // now it is unloaded
                    player.getClientView().unloadedChunk(cc);
                }
                return null;
            }
        });

    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(playerList.values());
    }

    public void handlePlayerOnGround(MainPlayer player, boolean onGround) {
        // TODO ...?
    }

    public void handlePlayerLook(MainPlayer player, float pitch, float yaw) {
        Location oldLoc = player.getLocation().clone();

        player.getLocation().setPitch(pitch);
        player.getLocation().setYaw(yaw);
        player.setOldLocation(oldLoc);

        sendPlayerUpdates(player, EntityUpdateReason.CHANGE_LOOK);
    }

    public void handlePlayerMove(MainPlayer player, Location newLoc) {
        // we have to reset pitch and yaw because the player is only moving!
        Location oldLoc = player.getLocation();
        newLoc.setYaw(oldLoc.getYaw());
        newLoc.setPitch(oldLoc.getPitch());

        validatePlayerMovement(player, newLoc);

        sendChunkUpdates(player);
        sendPlayerUpdates(player, EntityUpdateReason.CHANGE_POSITION);
    }

    public void handlePlayerMoveAndLook(MainPlayer player, Location newLoc) {
        validatePlayerMovement(player, newLoc);

        sendChunkUpdates(player);
        sendPlayerUpdates(player, EntityUpdateReason.CHANGE_LOOK_AND_POSITION);
    }

    private void validatePlayerMovement(MainPlayer player, Location newLoc) {
        Location oldLoc = player.getLocation().clone();
        player.setOldLocation(oldLoc);
        player.setLocation(newLoc);
        // TODO do what we promise and validate!
    }

    public void removePlayer(MainPlayer player) {
        playerList.remove(player.getName());
    }

}
