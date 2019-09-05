package message.response;

import picture.StashedPicture;

public class GameCreatedResponse extends Response {
    private static final long serialVersionUID = -5476441886572684946L;
    private final StashedPicture stashedPicture;

    public GameCreatedResponse(StashedPicture stashedPicture) {
        this.stashedPicture = stashedPicture;
    }

    public StashedPicture getStashedPicture() {
        return stashedPicture;
    }
}
