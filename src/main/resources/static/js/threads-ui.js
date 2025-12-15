/**
 * Threads UI Utilities
 * Common helper functions for UI components
 */

/**
 * Create a Threads-style avatar element
 * @param {string} username - The username to display
 * @param {string} size - Avatar size: 'sm', 'md', or 'lg' (default: 'md')
 * @returns {HTMLElement} The avatar element
 */
function createThreadsAvatar(username, size = 'md') {
  const avatar = document.createElement('div');
  avatar.className = `threads-avatar threads-avatar-${size}`;

  // Get first letter of username
  const initial = username ? username.charAt(0).toUpperCase() : '?';
  avatar.textContent = initial;

  // Generate gradient color based on username
  const hue = username
    ? username.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0) % 360
    : 0;

  avatar.style.background = `linear-gradient(135deg, hsl(${hue}, 70%, 50%), hsl(${(hue + 30) % 360}, 70%, 60%))`;

  return avatar;
}

/**
 * Format date to relative time (e.g., "2h ago", "3d ago")
 * @param {string|Date} dateString - The date to format
 * @returns {string} Formatted relative time
 */
function formatThreadsTime(dateString) {
  if (!dateString) return 'Just now';

  const date = new Date(dateString);
  const now = new Date();
  const diffMs = now - date;
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);

  if (diffSec < 60) return 'Just now';
  if (diffMin < 60) return `${diffMin}m`;
  if (diffHour < 24) return `${diffHour}h`;
  if (diffDay < 7) return `${diffDay}d`;
  if (diffDay < 30) return `${Math.floor(diffDay / 7)}w`;

  // For older dates, show formatted date
  return `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}`;
}

/**
 * Format date to full timestamp (YYYY-MM-DD HH:mm)
 * @param {string|Date} dateString - The date to format
 * @returns {string} Formatted timestamp
 */
function formatFullTimestamp(dateString) {
  if (!dateString) return '';

  const d = new Date(dateString);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

/**
 * Animate elements on scroll (fade in when visible)
 */
function animateOnScroll() {
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('threads-fade-in');
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.1 }
  );

  // Observe all cards
  document.querySelectorAll('.threads-card').forEach(card => {
    observer.observe(card);
  });
}

/**
 * Smooth scroll to element
 * @param {string|HTMLElement} target - Element or selector to scroll to
 * @param {number} offset - Offset from top in pixels (default: 80)
 */
function smoothScrollTo(target, offset = 80) {
  const element = typeof target === 'string'
    ? document.querySelector(target)
    : target;

  if (!element) return;

  const elementPosition = element.getBoundingClientRect().top;
  const offsetPosition = elementPosition + window.pageYOffset - offset;

  window.scrollTo({
    top: offsetPosition,
    behavior: 'smooth'
  });
}

/**
 * Truncate text to specified length
 * @param {string} text - Text to truncate
 * @param {number} maxLength - Maximum length
 * @param {string} suffix - Suffix to add (default: '...')
 * @returns {string} Truncated text
 */
function truncateText(text, maxLength, suffix = '...') {
  if (!text || text.length <= maxLength) return text;
  return text.substring(0, maxLength) + suffix;
}

/**
 * Escape HTML to prevent XSS
 * @param {string} unsafe - Unsafe string
 * @returns {string} Escaped string
 */
function escapeHtml(unsafe) {
  const div = document.createElement('div');
  div.textContent = unsafe;
  return div.innerHTML;
}

/**
 * Show a toast notification
 * @param {string} message - Message to display
 * @param {string} type - Type: 'success', 'error', 'info' (default: 'info')
 * @param {number} duration - Duration in ms (default: 3000)
 */
function showToast(message, type = 'info', duration = 3000) {
  // Create toast element
  const toast = document.createElement('div');
  toast.className = `threads-toast threads-toast-${type}`;
  toast.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: var(--threads-bg-primary);
    border: 1px solid var(--threads-border);
    border-radius: var(--threads-radius-md);
    padding: var(--threads-space-md) var(--threads-space-lg);
    box-shadow: 0 4px 12px var(--threads-shadow);
    z-index: 9999;
    max-width: 300px;
  `;

  toast.textContent = message;
  document.body.appendChild(toast);

  // Animate in
  toast.classList.add('threads-toast-enter');

  // Remove after duration
  setTimeout(() => {
    toast.classList.remove('threads-toast-enter');
    toast.classList.add('threads-toast-exit');
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

/**
 * Debounce function
 * @param {Function} func - Function to debounce
 * @param {number} wait - Wait time in ms
 * @returns {Function} Debounced function
 */
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

/**
 * Throttle function
 * @param {Function} func - Function to throttle
 * @param {number} limit - Time limit in ms
 * @returns {Function} Throttled function
 */
function throttle(func, limit) {
  let inThrottle;
  return function executedFunction(...args) {
    if (!inThrottle) {
      func(...args);
      inThrottle = true;
      setTimeout(() => inThrottle = false, limit);
    }
  };
}

/**
 * Check if element is in viewport
 * @param {HTMLElement} element - Element to check
 * @returns {boolean} True if in viewport
 */
function isInViewport(element) {
  const rect = element.getBoundingClientRect();
  return (
    rect.top >= 0 &&
    rect.left >= 0 &&
    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
    rect.right <= (window.innerWidth || document.documentElement.clientWidth)
  );
}

/**
 * Copy text to clipboard
 * @param {string} text - Text to copy
 * @returns {Promise<boolean>} Success status
 */
async function copyToClipboard(text) {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch (err) {
    // Fallback for older browsers
    const textarea = document.createElement('textarea');
    textarea.value = text;
    textarea.style.position = 'fixed';
    textarea.style.opacity = '0';
    document.body.appendChild(textarea);
    textarea.select();
    const success = document.execCommand('copy');
    document.body.removeChild(textarea);
    return success;
  }
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
  // Animate cards on scroll
  animateOnScroll();

  // Add smooth scroll to all anchor links
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
      e.preventDefault();
      const target = document.querySelector(this.getAttribute('href'));
      if (target) {
        smoothScrollTo(target);
      }
    });
  });
});

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
  module.exports = {
    createThreadsAvatar,
    formatThreadsTime,
    formatFullTimestamp,
    animateOnScroll,
    smoothScrollTo,
    truncateText,
    escapeHtml,
    showToast,
    debounce,
    throttle,
    isInViewport,
    copyToClipboard
  };
}
