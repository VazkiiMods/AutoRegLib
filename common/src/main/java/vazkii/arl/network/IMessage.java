package vazkii.arl.network;

import java.io.Serializable;

public interface IMessage extends Serializable {
	// TODO FIXME
	boolean receive(NetworkEvent.Context context);
}