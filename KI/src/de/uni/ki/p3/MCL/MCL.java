package de.uni.ki.p3.MCL;

import java.util.*;

import de.uni.ki.p3.robot.*;

public class MCL implements RobotListener
{
	private List<Particle> particles;
	private MCLConfiguration config;
	private MCLWeightFunction weighter;
	private MCLResampler resampler;
	private RangeMap map;
	private List<MCLListener> listener;
	
	public MCL(RangeMap map, MCLConfiguration config)
	{
		particles = new Vector<>();
		this.config = config;
		weighter = new MCLDefaultWeightFunction();
		resampler = new MCLDefaultResampler();
		this.map = map;
		listener = new ArrayList<>();
	}
	
	public void initializeParticles()
	{
		for(int i = 0; i < config.initialParticleCount; ++i)
		{
			Particle p = new Particle(
				new Position(
    				config.initialParticlePosX + config.random.nextDouble() * config.initialParticlePosWidth,
    				config.initialParticlePosY + config.random.nextDouble() * config.initialParticlePosHeight),
				config.minAngle + config.random.nextDouble() * (config.maxAngle - config.minAngle));
			particles.add(p);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotMoved(Robot robot, double dist)
	{
		for(Particle p : particles)
		{
			p.move(dist);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotRotated(Robot robot, double angle)
	{
		for(Particle p : particles)
		{
			p.rotate(angle);
		}
		
		fireEvent();
	}
	
	@Override
	public void robotTerminated(Robot ev3RobotDecorator)
	{
	}
	
	@Override
	public void robotMeasured(Robot robot, RobotMeasurement measurement)
	{
		for(Particle p : particles)
		{
			p.setWeight(weighter.calcWeight(this, measurement, p));
		}
		
		particles = resampler.resample(this, particles);
		
		fireEvent();
	}
	
	public void normalizeWeights()
	{
		double max = 0d;
		
		for(Particle p : particles)
		{
			max = Math.max(max, p.getWeight());
		}
		
		for(Particle p : particles)
		{
			p.setWeight(p.getWeight() / max);
		}
	}

	public List<Particle> getParticles()
	{
		return Collections.unmodifiableList(particles);
	}
	
	public void addMclListener(MCLListener l)
	{
		listener.add(l);
	}
	
	public void removeMclListener(MCLListener l)
	{
		listener.remove(l);
	}
	
	public void fireEvent()
	{
		for(MCLListener l : listener)
		{
			l.particlesChanged(this);
		}
	}
	
	public MCLWeightFunction getWeighter()
	{
		return weighter;
	}
	
	public void setWeighter(MCLWeightFunction weighter)
	{
		this.weighter = weighter;
	}
	
	public MCLResampler getResampler()
	{
		return resampler;
	}
	
	public void setResampler(MCLResampler resampler)
	{
		this.resampler = resampler;
	}
	
	public MCLConfiguration getConfig()
	{
		return config;
	}
	
	public RangeMap getMap()
	{
		return map;
	}
	
	public Particle getBest()
	{
		// TODO $DeH select considering all particles
		// i. e. maybe we have a heap full of nearly-best particles
		// than we should return one particle in the center of the heap
		return Collections.max(particles);
	}
}