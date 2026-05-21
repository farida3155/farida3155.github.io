import React, { useRef, useMemo } from 'react';
import { useFrame } from '@react-three/fiber';
import { Float, Sphere, MeshDistortMaterial, PerspectiveCamera } from '@react-three/drei';
import * as THREE from 'three';

const CloudBlob = ({ position, color, speed, distort, radius }) => {
  return (
    <Float speed={speed} rotationIntensity={0.5} floatIntensity={0.5}>
      <Sphere args={[radius, 32, 32]} position={position}>
        <MeshDistortMaterial
          color={color}
          speed={distort}
          distort={0.4}
          radius={1}
          emissive={color}
          emissiveIntensity={0.2}
          transparent
          opacity={0.7}
        />
      </Sphere>
    </Float>
  );
};

const FloatingParticles = ({ count = 40 }) => {
  const points = useMemo(() => {
    const p = new Float32Array(count * 3);
    for (let i = 0; i < count; i++) {
      p[i * 3] = (Math.random() - 0.5) * 20;
      p[i * 3 + 1] = (Math.random() - 0.5) * 20;
      p[i * 3 + 2] = (Math.random() - 0.5) * 15;
    }
    return p;
  }, [count]);

  const ref = useRef();
  useFrame((state) => {
    const t = state.clock.getElapsedTime();
    if (ref.current) {
        ref.current.rotation.y = t * 0.05;
        ref.current.rotation.x = t * 0.02;
    }
  });

  return (
    <points ref={ref}>
      <bufferGeometry>
        <bufferAttribute
          attach="attributes-position"
          count={points.length / 3}
          array={points}
          itemSize={3}
        />
      </bufferGeometry>
      <pointsMaterial
        size={0.05}
        color="#ffffff"
        transparent
        opacity={0.4}
        sizeAttenuation
      />
    </points>
  );
};

const CalmClouds = ({ isFocused }) => {
  const groupRef = useRef();
  
  // Create a few blobs with different pastel colors
  const blobs = useMemo(() => [
    { position: [-4, 2, -5], color: '#E0C3FC', speed: 1.5, distort: 2, radius: 2 },   // Lavender
    { position: [4, -2, -6], color: '#BDE0FE', speed: 1.2, distort: 1.5, radius: 2.5 }, // Light Blue
    { position: [0, -4, -8], color: '#FFD7BA', speed: 1.0, distort: 2.5, radius: 3 },   // Peach
    { position: [-6, -2, -10], color: '#C0FDFF', speed: 0.8, distort: 1.8, radius: 2.2 }, // Mint/Aqua
    { position: [6, 3, -12], color: '#FFC8DD', speed: 1.3, distort: 2.2, radius: 2.8 },  // Soft Pink
  ], []);

  useFrame((state) => {
    const t = state.clock.getElapsedTime();
    
    // Subtle camera drift
    state.camera.position.x = THREE.MathUtils.lerp(state.camera.position.x, isFocused ? 0 : Math.sin(t * 0.2) * 0.5, 0.05);
    state.camera.position.y = THREE.MathUtils.lerp(state.camera.position.y, isFocused ? 0 : Math.cos(t * 0.3) * 0.3, 0.05);
    state.camera.position.z = THREE.MathUtils.lerp(state.camera.position.z, isFocused ? 6 : 8, 0.05);
    
    state.camera.lookAt(0, 0, 0);

    // Slow down group movement if focused
    if (groupRef.current) {
        groupRef.current.rotation.y = t * (isFocused ? 0.02 : 0.05);
    }
  });

  return (
    <group ref={groupRef}>
      <PerspectiveCamera makeDefault position={[0, 0, 8]} fov={50} />
      <fog attach="fog" args={['#F3F1FB', 5, 20]} />
      
      <ambientLight intensity={0.8} />
      <directionalLight position={[10, 10, 5]} intensity={1} color="#ffffff" />
      <pointLight position={[-10, -10, -5]} intensity={0.5} color="#D6C8F5" />

      {blobs.map((blob, i) => (
        <CloudBlob key={i} {...blob} />
      ))}

      <FloatingParticles />
      
      {/* Background Sphere for the gradient feel */}
      <Sphere args={[25, 32, 32]} scale={[-1, 1, 1]}>
        <meshBasicMaterial color="#F3F1FB" side={THREE.BackSide} />
      </Sphere>
    </group>
  );
};

export default CalmClouds;
