/**
 * Theme Manager - Handles dark/light mode switching
 * Stores preference in localStorage and respects system preference
 */

class ThemeManager {
  constructor() {
    this.THEME_KEY = 'threads-theme';
    this.THEME_LIGHT = 'light';
    this.THEME_DARK = 'dark';

    // Initialize theme
    this.currentTheme = this.loadTheme();
    this.applyTheme(this.currentTheme, false);

    // Listen for system theme changes
    this.setupSystemThemeListener();
  }

  /**
   * Load theme from localStorage or system preference
   * @returns {string} 'light' or 'dark'
   */
  loadTheme() {
    // Check localStorage first
    const savedTheme = localStorage.getItem(this.THEME_KEY);
    if (savedTheme === this.THEME_LIGHT || savedTheme === this.THEME_DARK) {
      return savedTheme;
    }

    // Fall back to system preference
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    return prefersDark ? this.THEME_DARK : this.THEME_LIGHT;
  }

  /**
   * Apply theme to document
   * @param {string} theme - 'light' or 'dark'
   * @param {boolean} animate - Whether to animate the transition
   */
  applyTheme(theme, animate = true) {
    // Temporarily disable transitions for instant theme change
    if (!animate) {
      document.documentElement.classList.add('threads-no-transition');
    }

    // Set data-theme attribute
    document.documentElement.setAttribute('data-theme', theme);
    this.currentTheme = theme;

    // Save to localStorage
    localStorage.setItem(this.THEME_KEY, theme);

    // Re-enable transitions after a short delay
    if (!animate) {
      setTimeout(() => {
        document.documentElement.classList.remove('threads-no-transition');
      }, 50);
    }

    // Dispatch custom event for other scripts
    window.dispatchEvent(new CustomEvent('themeChanged', {
      detail: { theme }
    }));
  }

  /**
   * Toggle between light and dark theme
   * @returns {string} The new theme
   */
  toggle() {
    const newTheme = this.currentTheme === this.THEME_LIGHT
      ? this.THEME_DARK
      : this.THEME_LIGHT;

    this.applyTheme(newTheme, true);
    return newTheme;
  }

  /**
   * Get current theme
   * @returns {string} Current theme ('light' or 'dark')
   */
  getTheme() {
    return this.currentTheme;
  }

  /**
   * Check if current theme is dark
   * @returns {boolean}
   */
  isDark() {
    return this.currentTheme === this.THEME_DARK;
  }

  /**
   * Set up listener for system theme changes
   */
  setupSystemThemeListener() {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');

    // Only auto-switch if user hasn't manually set a preference
    mediaQuery.addEventListener('change', (e) => {
      const hasManualPreference = localStorage.getItem(this.THEME_KEY);
      if (!hasManualPreference) {
        const newTheme = e.matches ? this.THEME_DARK : this.THEME_LIGHT;
        this.applyTheme(newTheme, true);
      }
    });
  }

  /**
   * Create and inject theme toggle button
   * @param {HTMLElement} container - Container to append button to
   * @returns {HTMLElement} The created button
   */
  createToggleButton(container) {
    const button = document.createElement('button');
    button.className = 'threads-theme-toggle';
    button.setAttribute('aria-label', 'Toggle theme');
    button.setAttribute('title', 'Toggle dark/light mode');

    button.innerHTML = `
      <svg class="theme-icon theme-icon-light" fill="currentColor" viewBox="0 0 20 20">
        <path fill-rule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clip-rule="evenodd"></path>
      </svg>
      <svg class="theme-icon theme-icon-dark" fill="currentColor" viewBox="0 0 20 20">
        <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"></path>
      </svg>
    `;

    button.addEventListener('click', () => this.toggle());

    if (container) {
      container.appendChild(button);
    }

    return button;
  }
}

// Create global instance
window.themeManager = new ThemeManager();

// Auto-initialize toggle button when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
  // Look for theme toggle container
  const toggleContainer = document.getElementById('theme-toggle-container');
  if (toggleContainer) {
    window.themeManager.createToggleButton(toggleContainer);
  }
});

// Export for module usage (if needed)
if (typeof module !== 'undefined' && module.exports) {
  module.exports = ThemeManager;
}
