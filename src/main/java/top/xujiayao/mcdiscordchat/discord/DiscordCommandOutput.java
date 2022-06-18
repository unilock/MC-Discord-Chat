package top.xujiayao.mcdiscordchat.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Xujiayao
 */
public class DiscordCommandOutput implements CommandOutput {

	private final SlashCommandInteractionEvent e;
	private StringBuilder output = new StringBuilder("```\n");
	private long lastOutputMillis = 0;

	public DiscordCommandOutput(SlashCommandInteractionEvent e) {
		this.e = e;
	}

	@Override
	public void sendMessage(Text message) {
		long currentOutputMillis = System.currentTimeMillis();

		if (output.length() > 1500) {
			output.append("```");
			e.getChannel().sendMessage(output.toString()).queue();
			output = new StringBuilder("```\n");
		} else {
			output.append(message.getString()).append("\n");
		}

		if ((currentOutputMillis - lastOutputMillis) > 50) {
			new Thread(() -> new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					output.append("```");
					e.getChannel().sendMessage(output.toString()).queue();
					output = new StringBuilder("```\n");
				}
			}, 51)).start();
		}

		lastOutputMillis = currentOutputMillis;
	}

	@Override
	public boolean shouldReceiveFeedback() {
		return true;
	}

	@Override
	public boolean shouldTrackOutput() {
		return true;
	}

	@Override
	public boolean shouldBroadcastConsoleToOps() {
		return true;
	}

}
