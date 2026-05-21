import React, { useRef } from 'react';
import { useFrame } from '@react-three/fiber';
import { Sphere, MeshDistortMaterial } from '@react-three/drei';
import * as THREE from 'three';

export default function BreathingOrb() {
  const meshRef = useRef();

  useFrame(({ clock }) => {
    // 4 second inhale, 4 second exhale (8 second total loop)
    const elapsedTime = clock.getElapsedTime();
    // sine wave from 0.9 to 1.3
    const scale = 1.1 + Math.sin(elapsedTime * (Math.PI / 4)) * 0.2;

    if (meshRef.current) {
      meshRef.current.scale.set(scale, scale, scale);

      // Slowly rotate the orb
      meshRef.current.rotation.y = elapsedTime * 0.1;
      meshRef.current.rotation.z = elapsedTime * 0.05;
    }
  });

  return (
    <Sphere ref={meshRef} args={[1, 64, 64]} position={[0, 0, 0]}>
      {/* We use MeshDistortMaterial for a fluid, soft look */}
      <MeshDistortMaterial
        color="#D6C8F5" // soft purple base
        attach="material"
        distort={0.3} // Amount of distortion
        speed={1.5} // Speed of distortion animation
        roughness={0.2}
        metalness={0.1}
        transmission={0.8} // Glass-like effect
        thickness={1}
      />
    </Sphere>
  );
}
