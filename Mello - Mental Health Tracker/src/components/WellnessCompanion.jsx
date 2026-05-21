import React from "react";
import { motion } from "framer-motion";

export default function WellnessCompanion() {
  return (
    <div style={{ width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <motion.svg
        width="220" height="220" viewBox="0 0 220 220" fill="none"
        animate={{ y: [-5, 5, -5] }}
        transition={{ repeat: Infinity, duration: 5, ease: "easeInOut" }}
      >
        {/* Soft Background Glow/Circle */}
        <motion.circle
          cx="110" cy="110" r="90"
          fill="#F3E8FF"
          animate={{ scale: [1, 1.05, 1], opacity: [0.6, 0.8, 0.6] }}
          transition={{ repeat: Infinity, duration: 4 }}
        />

        {/* Abstract Journal / Notebook */}
        <path d="M70 60C70 54.4772 74.4772 50 80 50H140C145.523 50 150 54.4772 150 60V160C150 165.523 145.523 170 140 170H80C74.4772 170 70 165.523 70 160V60Z" fill="#FFFFFF" stroke="#E0BBE4" strokeWidth="4" />

        {/* Notebook Lines */}
        <path d="M85 75H135" stroke="#E0BBE4" strokeWidth="3" strokeLinecap="round" />
        <path d="M85 95H135" stroke="#E0BBE4" strokeWidth="3" strokeLinecap="round" />
        <path d="M85 115H115" stroke="#E0BBE4" strokeWidth="3" strokeLinecap="round" />

        {/* Minimal Coffee / Tea Cup next to it */}
        <path d="M140 130H160C165.523 130 170 134.477 170 140V145C170 153.284 163.284 160 155 160H140" stroke="#B4D4F8" strokeWidth="4" />
        <path d="M130 120H150V150C150 155.523 145.523 160 140 160H140C134.477 160 130 155.523 130 150V120Z" fill="#FFFFFF" stroke="#B4D4F8" strokeWidth="4" />

        {/* Floating sparkles/stars */}
        <motion.path
          d="M50 80L55 90L65 95L55 100L50 110L45 100L35 95L45 90L50 80Z"
          fill="#FFF2CC"
          animate={{ rotate: 180, scale: [0.8, 1.2, 0.8] }}
          transition={{ repeat: Infinity, duration: 3 }}
        />
        <motion.path
          d="M170 60L172 66L178 68L172 70L170 76L168 70L162 68L168 66L170 60Z"
          fill="#D5E8D4"
          animate={{ rotate: -180, scale: [1, 1.5, 1] }}
          transition={{ repeat: Infinity, duration: 4, delay: 1 }}
        />
      </motion.svg>
    </div>
  );
}
