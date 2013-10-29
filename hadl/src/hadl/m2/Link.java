package hadl.m2;

public abstract class Link {
    
    private final Linkable end1;
    
    private final Linkable end2;
    
    protected Link(final Linkable end1, final Linkable end2) {
        this.end1 = end1;
        this.end2 = end2;
        
        this.end1.plug(this);
        this.end2.plug(this);
    }
    
    public Linkable[] getEnds() {
        return new Linkable[] { this.end1, this.end2 };
    }
    
    public void send(final Linkable end, final Message msg) {
        Linkable receiver = this.end1;
        
        if (end.equals(receiver)) {
            receiver = this.end2;
        }
        
        receiver.receive(msg);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean eq = false;
        
        if (o instanceof Link) {
            Link link = (Link) o;
            eq = (link.end1.equals(this.end1)) && (link.end2.equals(link.end2));
        }
        
        return eq;
    }
}
