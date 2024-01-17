package top.xujiayao.mcdiscordchat.utils;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.node.TextNode;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.fellbaum.jemoji.EmojiManager;
import net.minecraft.text.Text;

import java.util.Map;

import static top.xujiayao.mcdiscordchat.Main.CONFIG;
import static top.xujiayao.mcdiscordchat.Main.SERVER;

public class PlaceholdersImpl {
    public static Text formatCommandNoticeText(String message, SlashCommandInteractionEvent e, String roleName) {
        Map<String, Text> placeholders = Map.of(
                "name", Text.literal((CONFIG.generic.useServerNickname ? e.getMember().getEffectiveName() : e.getMember().getUser().getName()).replace("\\", "\\\\").replace("\"", "\\\"")),
                "roleName", Text.literal(roleName),
                "roleColor", Text.literal(String.format("#%06X", (0xFFFFFF & e.getMember().getColorRaw()))),
                "command", Text.literal(e.getCommandString())
        );

        return Placeholders.parseText(
                TextNode.of(message),
                PlaceholderContext.of(SERVER),
                Placeholders.PLACEHOLDER_PATTERN_CUSTOM,
                placeholders
        );
    }

    public static Text formatOtherMessage(String message) {
       Map<String, Text> placeholders = Map.of(
               "server", Text.literal(CONFIG.multiServer.enable ? CONFIG.multiServer.name : "Discord"),
               "message", Text.literal("")
       );

        return Placeholders.parseText(
                TextNode.of(message),
                PlaceholderContext.of(SERVER),
                Placeholders.PLACEHOLDER_PATTERN_CUSTOM,
                placeholders
        );
    }

    public static Text formatResponseMessage(String s, String finalReferencedMessage, String referencedMessageTemp, String[] textAfterPlaceholder, Member referencedMember, String webhookName, String referencedMemberRoleName) {
        Map<String, Text> placeholders = Map.of(
                "message", Text.literal((CONFIG.generic.formatChatMessages ? finalReferencedMessage : EmojiManager.replaceAllEmojis(referencedMessageTemp, emoji -> emoji.getDiscordAliases().get(0)).replace("\"", "\\\""))
                        .replace("\n", "\n" + textAfterPlaceholder[0] + "}," + s.substring(1, s.indexOf("%message%")))),
                "server", Text.literal("Discord"),
                "name", Text.literal((referencedMember != null) ? (CONFIG.generic.useServerNickname ? referencedMember.getEffectiveName() : referencedMember.getUser().getName()).replace("\\", "\\\\").replace("\"", "\\\"") : webhookName),
                "roleName", Text.literal(referencedMemberRoleName),
                "roleColor", Text.literal(String.format("#%06X", (0xFFFFFF & ((referencedMember != null) ? referencedMember.getColorRaw() : Role.DEFAULT_COLOR_RAW))))
        );

        return Placeholders.parseText(
                TextNode.of(s),
                PlaceholderContext.of(SERVER),
                Placeholders.PLACEHOLDER_PATTERN_CUSTOM,
                placeholders
        );
    }
}
