/**
 *
 */

/**
 *
 */
public class MancalaController {
    private MancalaModel model;
    private MancalaView view;
    private BoardStyle currentStyle;
    
    public MancalaController(MancalaModel model, MancalaView view) {
        this.model = model;
        this.view = view;
        
        setupControllers();
    }
