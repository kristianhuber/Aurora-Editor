package aurora.world.sfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class ParticleEmitter {

	private static Random random = new Random();
	private final List<Particle> particles;
	private Vector3f location;
	private float spawningRate;
	private int particalLifeTime;
	private Vector3f gravity;
	private Vector3f initialVelocity;
	private boolean enable3D;
	private float velocityModifier;

	public ParticleEmitter() {

		this(new Vector3f(0, 0, 0), 3, 300, new Vector3f(0, 0.00003F, 0), true,
				new Vector3f(-0.00001F, 0, -0.00001F), 1.0F);
	}

	public ParticleEmitter(Vector3f location, float spawningRate,
			int particalLifeTime, Vector3f gravity, boolean enable3D,
			Vector3f initialVelocity, float velocityModifier) {
		this.location = location;
		this.spawningRate = spawningRate;
		this.particalLifeTime = particalLifeTime;
		this.gravity = gravity;
		this.initialVelocity = initialVelocity;
		this.enable3D = enable3D;
		this.velocityModifier = velocityModifier;

		this.particles = new ArrayList<Particle>((int) spawningRate
				* particalLifeTime);
	}

	public void update() {

		for (int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			particle.update(gravity);
			if (particle.isDestroyed()) {
				particles.remove(i);
				i--;
			}
		}
		if (enable3D) {
			if (!Mouse.isButtonDown(0)) {
				for (int i = 0; i < spawningRate; i++) {
					particles.add(generateNewParticle(0, 0));
				}
			}
		} else {

			float mouseX = ((float) Mouse.getX() / Display.getWidth() - 0.5F) * 2;
			float mouseY = ((float) Mouse.getY() / Display.getHeight() - 0.5F) * 2;
			if (Mouse.isButtonDown(0)) {
				location.set(mouseX, mouseY);
				int dx = Mouse.getDX();
				int dy = Mouse.getDY();

				for (int i = 0; i < spawningRate; i++) {
					particles.add(generateNewParticle(dx, dy));
				}
			}
		}
	}

	public void draw() {

		GL11.glPointSize(5F);
		
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glBegin(GL11.GL_POINTS);
		{
			for(Particle p : particles){
				float color = (float)p.expireTime / particalLifeTime;
				GL11.glColor4f(color, color, color, color);
				GL11.glVertex3f(p.position.x, p.position.y, p.position.z);
			}
		}
		GL11.glEnd();
		GL11.glPopAttrib();
	}

	private Particle generateNewParticle(int dx, int dy) {

		Vector3f particleLocation = new Vector3f(location);
		Vector3f particleVelocity = new Vector3f();
		float randomX = (float) random.nextDouble() - 0.5F;
		float randomY = (float) random.nextDouble() - 0.5F;
		float randomZ = 0;
		if (enable3D) {
			randomZ = (float) random.nextDouble() - 0.5F;
		}
		particleVelocity.x = (randomX + initialVelocity.x + dx / 10) / 120;
		particleVelocity.y = (randomY + initialVelocity.y + dy / 10) / 120;

		if (enable3D) {
			particleVelocity.z = (randomZ + initialVelocity.z) / 120;
		}
		particleVelocity.scale(velocityModifier);

		return new Particle(particleLocation, particleVelocity,
				particalLifeTime);
	}

	private class Particle {

		public Vector3f position;
		public Vector3f velocity;
		public int expireTime;

		private Particle(Vector3f position, Vector3f velocity, int expireTime) {

			this.position = position;
			this.velocity = velocity;
			this.expireTime = expireTime;
		}

		public boolean isDestroyed() {
			return expireTime <= 0;
		}

		public void update(Vector3f gravity) {

			position.translate(velocity.x, velocity.y, velocity.z);
			velocity.translate(gravity.x, gravity.y, gravity.z);
			expireTime -= 1;
		}
	}
}
