package dev.felnull.ttsvoice.tts.sayvoice;

import dev.felnull.fnjl.tuple.FNPair;
import dev.felnull.ttsvoice.util.DiscordUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VCEventSayVoice implements ISayVoice {
    private final EventType eventType;
    private final FNPair<Guild, Integer> guildAndBotNumber;
    private final User user;
    @Nullable
    private final GenericGuildVoiceUpdateEvent event;

    public VCEventSayVoice(EventType eventType, FNPair<Guild, Integer> guildAndBotNumber, User user) {
        this(eventType, guildAndBotNumber, user, null);
    }

    public VCEventSayVoice(EventType eventType, FNPair<Guild, Integer> guildAndBotNumber, User user, @Nullable GenericGuildVoiceUpdateEvent event) {
        this.eventType = eventType;
        this.guildAndBotNumber = guildAndBotNumber;
        this.user = user;
        this.event = event;
    }

    @Override
    public String getSayVoiceText() {
        return eventType.eventText.getText(guildAndBotNumber, user, event);
    }

    public EventType getEventType() {
        return eventType;
    }

    public GenericGuildVoiceUpdateEvent getEvent() {
        return event;
    }

    public static enum EventType {
        JOIN((guildAndBotNumber, user, e) -> DiscordUtils.getName(guildAndBotNumber.getRight(), guildAndBotNumber.getLeft(), user, user.getIdLong()) + "が参加しました"),
        LEAVE((guildAndBotNumber, user, e) -> DiscordUtils.getName(guildAndBotNumber.getRight(), guildAndBotNumber.getLeft(), user, user.getIdLong()) + "が退出しました"),
        MOVE_TO((guildAndBotNumber, user, e) -> DiscordUtils.getName(guildAndBotNumber.getRight(), guildAndBotNumber.getLeft(), user, user.getIdLong()) + "が" + DiscordUtils.getChannelName(e.getChannelJoined(), e.getMember(), "別のチャンネル") + "へ移動しました"),
        MOVE_FROM((guildAndBotNumber, user, e) -> DiscordUtils.getName(guildAndBotNumber.getRight(), guildAndBotNumber.getLeft(), user, user.getIdLong()) + "が" + DiscordUtils.getChannelName(e.getChannelLeft(), e.getMember(), "別のチャンネル") + "から移動してきました");
        private final EventText eventText;

        EventType(EventText eventText) {
            this.eventText = eventText;
        }
    }

    private static interface EventText {
        String getText(FNPair<Guild, Integer> guildAndBotNumber, User user, GenericGuildVoiceUpdateEvent event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VCEventSayVoice that = (VCEventSayVoice) o;
        return eventType == that.eventType && Objects.equals(guildAndBotNumber, that.guildAndBotNumber) && Objects.equals(user, that.user) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, guildAndBotNumber, user, event);
    }
}
