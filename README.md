# CST8277_Assignment_3
<h2>RMI-based 3-tier (GUI, Server, Database) networked multi-user version of the animated sprite application</h2>
<h3>Description</h3>
<p>• You are to develop an RMI-based 3-tier (GUI, Server, Database) networked multi-user version of the animated sprite application from assignment 1.</p>
<p>• The sprites will be implemented as persistent objects in a single database. A single central server component will be responsible for controlling the game, implementing the business rules (sprite creation, movement, bouncing, etc.), and accessing the database, but the server will not display a panel of moving sprites.</p>
<h3>Instructions</h3>
<h4>Phase 1</h4>
<p>• Implement the presentation and business tiers first (deal with colors as a last step).</p>
<p>• This phase is RMI from client to server, and multithreading on the server side to move the sprites.</p>
<p>• Class Sprite will need to be serializable, have x, y, dx, dy, and Color as attributes.</p>
<h4>Phase 2</h4>
<p>• Implement persistence using Hibernate + MySQL to save Sprites into a database table.</p>
<p>• Add annotations to class Sprite for persistence, you might need to research a way to save Color into the database table (with a conversion annotation) or save color as Red & Blue & Green columns in the database. Research and cite & reference your source(s) using IEEE reference style.</p>
<h3>Notes</h3>
<h4>GUI (Presentation Tier)</h4>
<p>• Swing, use the starter code from Assignment 1</p>
<p>• Producer and consumer with circle and collision detection is not required</p>
<p>• The presentation layer, the client application, will display the moving sprites. Many clients running on different machines will display the exact same sprite world to all current users.</p>
<p>• The sprites will be generated in a sequence of colors, regardless of what client created a sprite. This will be Red, Blue, Green, (repeating). Note: With the one server tracking the sequence of colors, and each client request to the server running on its own thread server-side, you will need to make sure that assigning of colors during Sprite creation is thread-safe (think synchronized code block rather than a synchronized method).</p>
<p>• The presentation layer will need to use RMI to initiate contact with the business layer, retrieving a remote object reference through which it can make requests.</p>
<p>• The presentation layer will not create Sprites, nor move them. When the user clicks a mouse, the presentation layer will ask the business layer to create a Sprite at the coordinates where the mouse was clicked.</p>
<p>• The presentation layer will need to display the Sprites as they exist as persistent objects on the server. The Sprites displayed on a client screen are never more than a small fraction of a second out of date compared to the database. Think loop in client that requests a new List<Sprite> from the server with Thread.Sleep (so that it does not flood the server with requests) before repainting the List<Sprite> retrieved).</p>
<p>• Your presentation layer will deal with the display, and it will have a JPanel, for example, and the presentation will need to ask the Server Side how big the JPanel should be. The Server does not have a JPanel, but it determines how big the JPanel should be. Account for the insets of the JFrame on the client.</p>
<h4>Server (Business Tier)</h4>
<p>• RMI, use the Hibernate_Demo_1 demo from the Hibernate lecture notes as a starting point.</p>
<p>• No GUI.</p>
<p>• Make a server component (business layer) by making a copy of your SpritePanel (either copied from your solution to Assignment 1, Part 1, or from the initial version distributed to you for Assignment 1). As well as any other necessary adjustments, be sure to remove the JPanel and JFrame and their associated code, because the server must not display any graphics.</p>
<p>• On start up the business layer should obtain the sprites from the database (do this in Phase2)</p>
<p>• The business layer will also need one infinite loop, running in a thread, that moves the sprites, then updates them in the database on each iteration of the loop.</p>
<p>• There is no need to implement multithreaded sprites on the server, nor is there any need to implement a producer / consumer. (The focus of this assignment is RMI and Hibernate, not multithreaded-producer-consumer).</p>
<p>• As with all RMI implementations, you will need an interface to be implemented by the remote object in the business layer. Your interface will need to support retrieving the current list of Sprites from the server, as well as creating a new Sprite on the server, and sending the size needed for the panel.</p>
<h4>Persistence (Data Tier)</h4>
<p>• Hibernate 5.2.7 + MySQL 5.7</p>
<p>• Use Hibernate 5.2.7 or newer with annotations and a hibernate.cfg.xml file.</p>
