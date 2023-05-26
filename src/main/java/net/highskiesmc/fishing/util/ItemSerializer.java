package net.highskiesmc.fishing.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public abstract class ItemSerializer {
    public static String serialize(ItemStack stack) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeObject(stack);

        byte[] bytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(bytes);
    }

    public static ItemStack deserialize(String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(input);

        ItemStack item = null;

        try {
            item = (ItemStack)dataInput.readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException("Unable to decode class type.", ex);
        }

        dataInput.close();
        return item;
    }
}
