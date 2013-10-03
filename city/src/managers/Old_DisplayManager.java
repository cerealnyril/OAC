package managers;

import grafx.Quad;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


//import static org.lwjgl.opengl.GL11.*;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.*;

import tools.ParamsGlobals;

import GLSL.ShaderLoader;

public class Old_DisplayManager implements Runnable{
	public enum State {INTRO, GAME, MAIN_MENU, MAP;}
	private static State state = State.MAP;
	boolean UP;
	boolean DOWN;
	boolean LEFT;
	boolean RIGHT;
	boolean BATS;
	boolean BLOCS;
	boolean ROADS;
	boolean RAILS;
	boolean TAB;
	boolean ESC;
	boolean MOINS;
	boolean PLUS;
	boolean F1;
	boolean F2;
	boolean F3;
	boolean F4;
	boolean F5;
	boolean F6;
	boolean MULT;
	boolean DIV;
	boolean SPACE;
	private Quad q = new Quad(0,0,0,0,0,0);
	private int[] bufferIds = new int[3];


	public Old_DisplayManager(){}

	@Override
	public void run() {
/*		glInit();
		ParamsGlobals.MANAGER.setupDisplay();
		ParamsGlobals.MANAGER.setupMap();
		ParamsGlobals.MANAGER.setupCamera();
//		bufferIds = RenderManager.buffer(q.getElements(), new int[]{0,1,2,2,3,0}, 6);
		ParamsGlobals.MANAGER.setupMultiverse();
		
		update();*/
	}

	
/*******************************INITIALISATION************************************************/

	/** Initialisation de l'affichage OpenGl*/
	private void glInit(){
		// Setup an OpenGL context with API version 3.2
/*		try {
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(3, 2);
			contextAtrributes.withForwardCompatible(true);
			contextAtrributes.withProfileCore(true);
			
			Display.setDisplayMode(new DisplayMode(Globals.RES_WIDTH, Globals.RES_HEIGHT));
			Display.setTitle(Globals.VERSION);
			Display.create(pixelFormat, contextAtrributes);
			
			GL11.glViewport(0, 0, Globals.RES_WIDTH, Globals.RES_HEIGHT);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Setup an XNA like background color
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, Globals.RES_WIDTH, Globals.RES_HEIGHT);
		this.exitOnGLError("setupOpenGL");
		if (Globals.GLINFO)glInfo();
*/	}
	
	/**donne pas mal d'infos sur les capacités de la CG*/
    private void glInfo() {
/*		ContextCapabilities caps = GLContext.getCapabilities();

		System.out.println("\nGL_AMD_blend_minmax_factor: " + caps.GL_AMD_blend_minmax_factor);		
		System.out.println("GL_AMD_conservative_depth: " + caps.GL_AMD_conservative_depth);		
		System.out.println("GL_AMD_debug_output: " + caps.GL_AMD_debug_output);		
		System.out.println("GL_AMD_depth_clamp_separate: " + caps.GL_AMD_depth_clamp_separate);		
		System.out.println("GL_AMD_draw_buffers_blend: " + caps.GL_AMD_draw_buffers_blend);		
		System.out.println("GL_AMD_multi_draw_indirect: " + caps.GL_AMD_multi_draw_indirect);		
		System.out.println("GL_AMD_name_gen_delete: " + caps.GL_AMD_name_gen_delete);		
		System.out.println("GL_AMD_performance_monitor: " + caps.GL_AMD_performance_monitor);		
		System.out.println("GL_AMD_pinned_memory: " + caps.GL_AMD_pinned_memory);		
		System.out.println("GL_AMD_sample_positions: " + caps.GL_AMD_sample_positions);		
		System.out.println("GL_AMD_seamless_cubemap_per_texture: " + caps.GL_AMD_seamless_cubemap_per_texture);		
		System.out.println("GL_AMD_shader_stencil_export: " + caps.GL_AMD_shader_stencil_export);		
		System.out.println("GL_AMD_stencil_operation_extended: " + caps.GL_AMD_stencil_operation_extended);		
		System.out.println("GL_AMD_texture_texture4: " + caps.GL_AMD_texture_texture4);		
		System.out.println("GL_AMD_transform_feedback3_lines_triangles: " + caps.GL_AMD_transform_feedback3_lines_triangles);		
		System.out.println("GL_AMD_vertex_shader_layer: " + caps.GL_AMD_vertex_shader_layer);		
		System.out.println("GL_AMD_vertex_shader_tessellator: " + caps.GL_AMD_vertex_shader_tessellator);		
		System.out.println("GL_AMD_vertex_shader_viewport_index: " + caps.GL_AMD_vertex_shader_viewport_index);		
		System.out.println("\nGL_APPLE_aux_depth_stencil: " + caps.GL_APPLE_aux_depth_stencil);		
		System.out.println("GL_APPLE_client_storage: " + caps.GL_APPLE_client_storage);		
		System.out.println("GL_APPLE_element_array: " + caps.GL_APPLE_element_array);		
		System.out.println("GL_APPLE_fence: " + caps.GL_APPLE_fence);		
		System.out.println("GL_APPLE_float_pixels: " + caps.GL_APPLE_float_pixels);		
		System.out.println("GL_APPLE_flush_buffer_range: " + caps.GL_APPLE_flush_buffer_range);		
		System.out.println("GL_APPLE_object_purgeable: " + caps.GL_APPLE_object_purgeable);		
		System.out.println("GL_APPLE_packed_pixels: " + caps.GL_APPLE_packed_pixels);		
		System.out.println("GL_APPLE_rgb_422: " + caps.GL_APPLE_rgb_422);		
		System.out.println("GL_APPLE_row_bytes: " + caps.GL_APPLE_row_bytes);		
		System.out.println("GL_APPLE_texture_range: " + caps.GL_APPLE_texture_range);		
		System.out.println("GL_APPLE_vertex_array_object: " + caps.GL_APPLE_vertex_array_object);		
		System.out.println("GL_APPLE_vertex_array_range: " + caps.GL_APPLE_vertex_array_range);		
		System.out.println("GL_APPLE_vertex_program_evaluators: " + caps.GL_APPLE_vertex_program_evaluators);		
		System.out.println("GL_APPLE_ycbcr_422: " + caps.GL_APPLE_ycbcr_422);		
		System.out.println("\nGL_ARB_base_instance: " + caps.GL_ARB_base_instance);		
		System.out.println("GL_ARB_blend_func_extended: " + caps.GL_ARB_blend_func_extended);		
		System.out.println("GL_ARB_cl_event: " + caps.GL_ARB_cl_event);		
		System.out.println("GL_ARB_color_buffer_float: " + caps.GL_ARB_color_buffer_float);		
		System.out.println("GL_ARB_compatibility: " + caps.GL_ARB_compatibility);		
		System.out.println("GL_ARB_compressed_texture_pixel_storage: " + caps.GL_ARB_compressed_texture_pixel_storage);		
		System.out.println("GL_ARB_conservative_depth: " + caps.GL_ARB_conservative_depth);		
		System.out.println("GL_ARB_copy_buffer: " + caps.GL_ARB_copy_buffer);		
		System.out.println("GL_ARB_debug_output: " + caps.GL_ARB_debug_output);		
		System.out.println("GL_ARB_depth_buffer_float: " + caps.GL_ARB_depth_buffer_float);		
		System.out.println("GL_ARB_depth_clamp: " + caps.GL_ARB_depth_clamp);		
		System.out.println("GL_ARB_depth_texture: " + caps.GL_ARB_depth_texture);		
		System.out.println("GL_ARB_draw_buffers: " + caps.GL_ARB_draw_buffers);		
		System.out.println("GL_ARB_draw_buffers_blend: " + caps.GL_ARB_draw_buffers_blend);		
		System.out.println("GL_ARB_draw_elements_base_vertex: " + caps.GL_ARB_draw_elements_base_vertex);		
		System.out.println("GL_ARB_draw_indirect: " + caps.GL_ARB_draw_indirect);		
		System.out.println("GL_ARB_draw_instanced: " + caps.GL_ARB_draw_instanced);		
		System.out.println("GL_ARB_ES2_compatibility: " + caps.GL_ARB_ES2_compatibility);		
		System.out.println("GL_ARB_explicit_attrib_location: " + caps.GL_ARB_explicit_attrib_location);		
		System.out.println("GL_ARB_fragment_coord_conventions: " + caps.GL_ARB_fragment_coord_conventions);		
		System.out.println("GL_ARB_fragment_program: " + caps.GL_ARB_fragment_program);		
		System.out.println("GL_ARB_fragment_program_shadow: " + caps.GL_ARB_fragment_program_shadow);		
		System.out.println("GL_ARB_fragment_shader: " + caps.GL_ARB_fragment_shader);		
		System.out.println("GL_ARB_framebuffer_object: " + caps.GL_ARB_framebuffer_object);		
		System.out.println("GL_ARB_framebuffer_sRGB: " + caps.GL_ARB_framebuffer_sRGB);		
		System.out.println("GL_ARB_geometry_shader4: " + caps.GL_ARB_geometry_shader4);		
		System.out.println("GL_ARB_get_program_binary: " + caps.GL_ARB_get_program_binary);		
		System.out.println("GL_ARB_gpu_shader5: " + caps.GL_ARB_gpu_shader5);		
		System.out.println("GL_ARB_gpu_shader_fp64: " + caps.GL_ARB_gpu_shader_fp64);		
		System.out.println("GL_ARB_half_float_pixel: " + caps.GL_ARB_half_float_pixel);		
		System.out.println("GL_ARB_half_float_vertex: " + caps.GL_ARB_half_float_vertex);		
		System.out.println("GL_ARB_imaging: " + caps.GL_ARB_imaging);		
		System.out.println("GL_ARB_instanced_arrays: " + caps.GL_ARB_instanced_arrays);		
		System.out.println("GL_ARB_internalformat_query: " + caps.GL_ARB_internalformat_query);		
		System.out.println("GL_ARB_map_buffer_alignment: " + caps.GL_ARB_map_buffer_alignment);		
		System.out.println("GL_ARB_map_buffer_range: " + caps.GL_ARB_map_buffer_range);		
		System.out.println("GL_ARB_matrix_palette: " + caps.GL_ARB_matrix_palette);		
		System.out.println("GL_ARB_multisample: " + caps.GL_ARB_multisample);		
		System.out.println("GL_ARB_multitexture: " + caps.GL_ARB_multitexture);		
		System.out.println("GL_ARB_occlusion_query: " + caps.GL_ARB_occlusion_query);		
		System.out.println("GL_ARB_occlusion_query2: " + caps.GL_ARB_occlusion_query2);		
		System.out.println("GL_ARB_pixel_buffer_object: " + caps.GL_ARB_pixel_buffer_object);		
		System.out.println("GL_ARB_point_parameters: " + caps.GL_ARB_point_parameters);		
		System.out.println("GL_ARB_point_sprite: " + caps.GL_ARB_point_sprite);		
		System.out.println("GL_ARB_provoking_vertex: " + caps.GL_ARB_provoking_vertex);		
		System.out.println("GL_ARB_robustness: " + caps.GL_ARB_robustness);		
		System.out.println("GL_ARB_sample_shading: " + caps.GL_ARB_sample_shading);		
		System.out.println("GL_ARB_sampler_objects: " + caps.GL_ARB_sampler_objects);		
		System.out.println("GL_ARB_seamless_cube_map: " + caps.GL_ARB_seamless_cube_map);		
		System.out.println("GL_ARB_separate_shader_objects: " + caps.GL_ARB_separate_shader_objects);		
		System.out.println("GL_ARB_shader_atomic_counters: " + caps.GL_ARB_shader_atomic_counters);		
		System.out.println("GL_ARB_shader_bit_encoding: " + caps.GL_ARB_shader_bit_encoding);		
		System.out.println("GL_ARB_shader_image_load_store: " + caps.GL_ARB_shader_image_load_store);		
		System.out.println("GL_ARB_shader_objects: " + caps.GL_ARB_shader_objects);		
		System.out.println("GL_ARB_shader_precision: " + caps.GL_ARB_shader_precision);		
		System.out.println("GL_ARB_shader_stencil_export: " + caps.GL_ARB_shader_stencil_export);		
		System.out.println("GL_ARB_shader_subroutine: " + caps.GL_ARB_shader_subroutine);		
		System.out.println("GL_ARB_shader_texture_lod: " + caps.GL_ARB_shader_texture_lod);		
		System.out.println("GL_ARB_shading_language_100: " + caps.GL_ARB_shading_language_100);		
		System.out.println("GL_ARB_shading_language_420pack: " + caps.GL_ARB_shading_language_420pack);		
		System.out.println("GL_ARB_shading_language_include: " + caps.GL_ARB_shading_language_include);		
		System.out.println("GL_ARB_shading_language_packing: " + caps.GL_ARB_shading_language_packing);		
		System.out.println("GL_ARB_shadow: " + caps.GL_ARB_shadow);		
		System.out.println("GL_ARB_shadow_ambient: " + caps.GL_ARB_shadow_ambient);		
		System.out.println("GL_ARB_sync: " + caps.GL_ARB_sync);		
		System.out.println("GL_ARB_tessellation_shader: " + caps.GL_ARB_tessellation_shader);		
		System.out.println("GL_ARB_texture_border_clamp: " + caps.GL_ARB_texture_border_clamp);		
		System.out.println("GL_ARB_texture_buffer_object: " + caps.GL_ARB_texture_buffer_object);		
		System.out.println("GL_ARB_texture_buffer_object_rgb32: " + caps.GL_ARB_texture_buffer_object_rgb32);		
		System.out.println("GL_ARB_texture_compression: " + caps.GL_ARB_texture_compression);		
		System.out.println("GL_ARB_texture_compression_bptc: " + caps.GL_ARB_texture_compression_bptc);
		System.out.println("GL_ARB_texture_compression_rgtc: " + caps.GL_ARB_texture_compression_rgtc);
		System.out.println("GL_ARB_texture_cube_map: " + caps.GL_ARB_texture_cube_map);
		System.out.println("GL_ARB_texture_cube_map_array: " + caps.GL_ARB_texture_cube_map_array);
		System.out.println("GL_ARB_texture_env_add: " + caps.GL_ARB_texture_env_add);
		System.out.println("GL_ARB_texture_env_combine: " + caps.GL_ARB_texture_env_combine);
		System.out.println("GL_ARB_texture_env_crossbar: " + caps.GL_ARB_texture_env_crossbar);
		System.out.println("GL_ARB_texture_env_dot3: " + caps.GL_ARB_texture_env_dot3);
		System.out.println("GL_ARB_texture_float: " + caps.GL_ARB_texture_float);
		System.out.println("GL_ARB_texture_gather: " + caps.GL_ARB_texture_gather);
		System.out.println("GL_ARB_texture_mirrored_repeat: " + caps.GL_ARB_texture_mirrored_repeat);
		System.out.println("GL_ARB_texture_multisample: " + caps.GL_ARB_texture_multisample);
		System.out.println("GL_ARB_texture_non_power_of_two: " + caps.GL_ARB_texture_non_power_of_two);
		System.out.println("GL_ARB_texture_query_lod: " + caps.GL_ARB_texture_query_lod);
		System.out.println("GL_ARB_texture_rectangle: " + caps.GL_ARB_texture_rectangle);
		System.out.println("GL_ARB_texture_rg: " + caps.GL_ARB_texture_rg);
		System.out.println("GL_ARB_texture_rgb10_a2ui: " + caps.GL_ARB_texture_rgb10_a2ui);
		System.out.println("GL_ARB_texture_storage: " + caps.GL_ARB_texture_storage);
		System.out.println("GL_ARB_texture_swizzle: " + caps.GL_ARB_texture_swizzle);
		System.out.println("GL_ARB_timer_query: " + caps.GL_ARB_timer_query);
		System.out.println("GL_ARB_transform_feedback2: " + caps.GL_ARB_transform_feedback2);
		System.out.println("GL_ARB_transform_feedback3: " + caps.GL_ARB_transform_feedback3);
		System.out.println("GL_ARB_transform_feedback_instanced: " + caps.GL_ARB_transform_feedback_instanced);
		System.out.println("GL_ARB_transpose_matrix: " + caps.GL_ARB_transpose_matrix);
		System.out.println("GL_ARB_uniform_buffer_object: " + caps.GL_ARB_uniform_buffer_object);
		System.out.println("GL_ARB_vertex_array_bgra: " + caps.GL_ARB_vertex_array_bgra);
		System.out.println("GL_ARB_vertex_array_object: " + caps.GL_ARB_vertex_array_object);
		System.out.println("GL_ARB_vertex_attrib_64bit: " + caps.GL_ARB_vertex_attrib_64bit);
		System.out.println("GL_ARB_vertex_blend: " + caps.GL_ARB_vertex_blend);
		System.out.println("GL_ARB_vertex_buffer_object: " + caps.GL_ARB_vertex_buffer_object);
		System.out.println("GL_ARB_vertex_program: " + caps.GL_ARB_vertex_program);
		System.out.println("GL_ARB_vertex_shader: " + caps.GL_ARB_vertex_shader);
		System.out.println("GL_ARB_vertex_shader: " + caps.GL_ARB_vertex_shader);
		System.out.println("GL_ARB_vertex_type_2_10_10_10_rev: " + caps.GL_ARB_vertex_type_2_10_10_10_rev);
		System.out.println("GL_ARB_viewport_array: " + caps.GL_ARB_viewport_array);
		System.out.println("GL_ARB_window_pos: " + caps.GL_ARB_window_pos);
		System.out.println("\nGL_ATI_draw_buffers: " + caps.GL_ATI_draw_buffers);
		System.out.println("GL_ATI_element_array: " + caps.GL_ATI_element_array);
		System.out.println("GL_ATI_envmap_bumpmap: " + caps.GL_ATI_envmap_bumpmap);
		System.out.println("GL_ATI_fragment_shader: " + caps.GL_ATI_fragment_shader);
		System.out.println("GL_ATI_map_object_buffer: " + caps.GL_ATI_map_object_buffer);
		System.out.println("GL_ATI_meminfo: " + caps.GL_ATI_meminfo);
		System.out.println("GL_ATI_pn_triangles: " + caps.GL_ATI_pn_triangles);
		System.out.println("GL_ATI_separate_stencil: " + caps.GL_ATI_separate_stencil);
		System.out.println("GL_ATI_shader_texture_lod: " + caps.GL_ATI_shader_texture_lod);
		System.out.println("GL_ATI_text_fragment_shader: " + caps.GL_ATI_text_fragment_shader);
		System.out.println("GL_ATI_texture_compression_3dc: " + caps.GL_ATI_texture_compression_3dc);
		System.out.println("GL_ATI_texture_env_combine3: " + caps.GL_ATI_texture_env_combine3);
		System.out.println("GL_ATI_texture_float: " + caps.GL_ATI_texture_float);
		System.out.println("GL_ATI_texture_mirror_once: " + caps.GL_ATI_texture_mirror_once);
		System.out.println("GL_ATI_vertex_array_object: " + caps.GL_ATI_vertex_array_object);
		System.out.println("GL_ATI_vertex_attrib_array_object: " + caps.GL_ATI_vertex_attrib_array_object);
		System.out.println("GL_ATI_vertex_streams: " + caps.GL_ATI_vertex_streams);
		System.out.println("\nGL_EXT_abgr: " + caps.GL_EXT_abgr);
		System.out.println("GL_EXT_bgra: " + caps.GL_EXT_bgra);
		System.out.println("GL_EXT_bindable_uniform: " + caps.GL_EXT_bindable_uniform);
		System.out.println("GL_EXT_blend_color: " + caps.GL_EXT_blend_color);
		System.out.println("GL_EXT_blend_equation_separate: " + caps.GL_EXT_blend_equation_separate);
		System.out.println("GL_EXT_blend_func_separate: " + caps.GL_EXT_blend_func_separate);
		System.out.println("GL_EXT_blend_minmax: " + caps.GL_EXT_blend_minmax);
		System.out.println("GL_EXT_blend_subtract: " + caps.GL_EXT_blend_subtract);
		System.out.println("GL_EXT_Cg_shader: " + caps.GL_EXT_Cg_shader);
		System.out.println("GL_EXT_compiled_vertex_array: " + caps.GL_EXT_compiled_vertex_array);
		System.out.println("GL_EXT_Cg_shader: " + caps.GL_EXT_Cg_shader);
		System.out.println("GL_EXT_depth_bounds_test: " + caps.GL_EXT_depth_bounds_test);
		System.out.println("GL_EXT_direct_state_access: " + caps.GL_EXT_direct_state_access);
		System.out.println("GL_EXT_draw_buffers2: " + caps.GL_EXT_draw_buffers2);
		System.out.println("GL_EXT_draw_instanced: " + caps.GL_EXT_draw_instanced);
		System.out.println("GL_EXT_draw_range_elements: " + caps.GL_EXT_draw_range_elements);
		System.out.println("GL_EXT_fog_coord: " + caps.GL_EXT_fog_coord);
		System.out.println("GL_EXT_framebuffer_blit: " + caps.GL_EXT_framebuffer_blit);
		System.out.println("GL_EXT_framebuffer_multisample: " + caps.GL_EXT_framebuffer_multisample);
		System.out.println("GL_EXT_framebuffer_multisample_blit_scaled: " + caps.GL_EXT_framebuffer_multisample_blit_scaled);
		System.out.println("GL_EXT_framebuffer_object: " + caps.GL_EXT_framebuffer_object);
		System.out.println("GL_EXT_framebuffer_sRGB: " + caps.GL_EXT_framebuffer_sRGB);
		System.out.println("GL_EXT_geometry_shader4: " + caps.GL_EXT_geometry_shader4);
		System.out.println("GL_EXT_gpu_program_parameters: " + caps.GL_EXT_gpu_program_parameters);
		System.out.println("GL_EXT_gpu_shader4: " + caps.GL_EXT_gpu_shader4);
		System.out.println("GL_EXT_multi_draw_arrays: " + caps.GL_EXT_multi_draw_arrays);
		System.out.println("GL_EXT_packed_depth_stencil: " + caps.GL_EXT_packed_depth_stencil);
		System.out.println("GL_EXT_packed_float: " + caps.GL_EXT_packed_float);
		System.out.println("GL_EXT_packed_pixels: " + caps.GL_EXT_packed_pixels);
		System.out.println("GL_EXT_paletted_texture: " + caps.GL_EXT_paletted_texture);
		System.out.println("GL_EXT_pixel_buffer_object: " + caps.GL_EXT_pixel_buffer_object);
		System.out.println("GL_EXT_point_parameters: " + caps.GL_EXT_point_parameters);
		System.out.println("GL_EXT_provoking_vertex: " + caps.GL_EXT_provoking_vertex);
		System.out.println("GL_EXT_rescale_normal: " + caps.GL_EXT_rescale_normal);
		System.out.println("GL_EXT_secondary_color: " + caps.GL_EXT_secondary_color);
		System.out.println("GL_EXT_separate_shader_objects: " + caps.GL_EXT_separate_shader_objects);
		System.out.println("GL_EXT_separate_specular_color: " + caps.GL_EXT_separate_specular_color);
		System.out.println("GL_EXT_shader_image_load_store: " + caps.GL_EXT_shader_image_load_store);
		System.out.println("GL_EXT_shadow_funcs: " + caps.GL_EXT_shadow_funcs);
		System.out.println("GL_EXT_shared_texture_palette: " + caps.GL_EXT_shared_texture_palette);
		System.out.println("GL_EXT_stencil_clear_tag: " + caps.GL_EXT_stencil_clear_tag);
		System.out.println("GL_EXT_stencil_two_side: " + caps.GL_EXT_stencil_two_side);
		System.out.println("GL_EXT_stencil_wrap: " + caps.GL_EXT_stencil_wrap);
		System.out.println("GL_EXT_texture_3d: " + caps.GL_EXT_texture_3d);
		System.out.println("GL_EXT_texture_array: " + caps.GL_EXT_texture_array);
		System.out.println("GL_EXT_texture_buffer_object: " + caps.GL_EXT_texture_buffer_object);
		System.out.println("GL_EXT_texture_compression_latc: " + caps.GL_EXT_texture_compression_latc);
		System.out.println("GL_EXT_texture_compression_rgtc: " + caps.GL_EXT_texture_compression_rgtc);
		System.out.println("GL_EXT_texture_compression_s3tc: " + caps.GL_EXT_texture_compression_s3tc);
		System.out.println("GL_EXT_texture_env_combine: " + caps.GL_EXT_texture_env_combine);
		System.out.println("GL_EXT_texture_env_dot3: " + caps.GL_EXT_texture_env_dot3);
		System.out.println("GL_EXT_texture_filter_anisotropic: " + caps.GL_EXT_texture_filter_anisotropic);
		System.out.println("GL_EXT_texture_integer: " + caps.GL_EXT_texture_integer);
		System.out.println("GL_EXT_texture_lod_bias: " + caps.GL_EXT_texture_lod_bias);
		System.out.println("GL_EXT_texture_mirror_clamp: " + caps.GL_EXT_texture_mirror_clamp);
		System.out.println("GL_EXT_texture_rectangle: " + caps.GL_EXT_texture_rectangle);
		System.out.println("GL_EXT_texture_shared_exponent: " + caps.GL_EXT_texture_shared_exponent);
		System.out.println("GL_EXT_texture_snorm: " + caps.GL_EXT_texture_snorm);
		System.out.println("GL_EXT_texture_sRGB: " + caps.GL_EXT_texture_sRGB);
		System.out.println("GL_EXT_texture_sRGB_decode: " + caps.GL_EXT_texture_sRGB_decode);
		System.out.println("GL_EXT_texture_swizzle: " + caps.GL_EXT_texture_swizzle);
		System.out.println("GL_EXT_timer_query: " + caps.GL_EXT_timer_query);
		System.out.println("GL_EXT_transform_feedback: " + caps.GL_EXT_transform_feedback);
		System.out.println("GL_EXT_vertex_array_bgra: " + caps.GL_EXT_vertex_array_bgra);
		System.out.println("GL_EXT_vertex_attrib_64bit: " + caps.GL_EXT_vertex_attrib_64bit);
		System.out.println("GL_EXT_vertex_shader: " + caps.GL_EXT_vertex_shader);
		System.out.println("GL_EXT_vertex_weighting: " + caps.GL_EXT_vertex_weighting);
		System.out.println("\nGL_GREMEDY_string_marker: " + caps.GL_GREMEDY_string_marker);
		System.out.println("\nGL_HP_occlusion_test: " + caps.GL_HP_occlusion_test);
		System.out.println("\nGL_IBM_rasterpos_clip: " + caps.GL_IBM_rasterpos_clip);
		System.out.println("\nGL_NV_bindless_texture: " + caps.GL_NV_bindless_texture);
		System.out.println("GL_NV_blend_square: " + caps.GL_NV_blend_square);
		System.out.println("GL_NV_conditional_render: " + caps.GL_NV_conditional_render);
		System.out.println("GL_NV_copy_depth_to_color: " + caps.GL_NV_copy_depth_to_color);
		System.out.println("GL_NV_copy_image: " + caps.GL_NV_copy_image);
		System.out.println("GL_NV_depth_buffer_float: " + caps.GL_NV_depth_buffer_float);
		System.out.println("GL_NV_depth_clamp: " + caps.GL_NV_depth_clamp);
		System.out.println("GL_NV_evaluators: " + caps.GL_NV_evaluators);
		System.out.println("GL_NV_explicit_multisample: " + caps.GL_NV_explicit_multisample);
		System.out.println("GL_NV_fence: " + caps.GL_NV_fence);
		System.out.println("GL_NV_float_buffer: " + caps.GL_NV_float_buffer);
		System.out.println("GL_NV_fog_distance: " + caps.GL_NV_fog_distance);
		System.out.println("GL_NV_fragment_program: " + caps.GL_NV_fragment_program);
		System.out.println("GL_NV_fragment_program2: " + caps.GL_NV_fragment_program2);
		System.out.println("GL_NV_fragment_program4: " + caps.GL_NV_fragment_program4);
		System.out.println("GL_NV_fragment_program_option: " + caps.GL_NV_fragment_program_option);
		System.out.println("GL_NV_framebuffer_multisample_coverage: " + caps.GL_NV_framebuffer_multisample_coverage);
		System.out.println("GL_NV_geometry_program4: " + caps.GL_NV_geometry_program4);
		System.out.println("GL_NV_geometry_shader4: " + caps.GL_NV_geometry_shader4);
		System.out.println("GL_NV_gpu_program4: " + caps.GL_NV_gpu_program4);
		System.out.println("GL_NV_gpu_program5: " + caps.GL_NV_gpu_program5);
		System.out.println("GL_NV_gpu_shader5: " + caps.GL_NV_gpu_shader5);
		System.out.println("GL_NV_half_float: " + caps.GL_NV_half_float);
		System.out.println("GL_NV_light_max_exponent: " + caps.GL_NV_light_max_exponent);
		System.out.println("GL_NV_multisample_coverage: " + caps.GL_NV_multisample_coverage);
		System.out.println("GL_NV_multisample_filter_hint: " + caps.GL_NV_multisample_filter_hint);
		System.out.println("GL_NV_occlusion_query: " + caps.GL_NV_occlusion_query);
		System.out.println("GL_NV_packed_depth_stencil: " + caps.GL_NV_packed_depth_stencil);
		System.out.println("GL_NV_parameter_buffer_object: " + caps.GL_NV_parameter_buffer_object);
		System.out.println("GL_NV_parameter_buffer_object2: " + caps.GL_NV_parameter_buffer_object2);
		System.out.println("GL_NV_path_rendering: " + caps.GL_NV_path_rendering);
		System.out.println("GL_NV_pixel_data_range: " + caps.GL_NV_pixel_data_range);
		System.out.println("GL_NV_point_sprite: " + caps.GL_NV_point_sprite);
		System.out.println("GL_NV_present_video: " + caps.GL_NV_present_video);
		System.out.println("GL_NV_primitive_restart: " + caps.GL_NV_primitive_restart);
		System.out.println("GL_NV_register_combiners: " + caps.GL_NV_register_combiners);
		System.out.println("GL_NV_register_combiners2: " + caps.GL_NV_register_combiners2);
		System.out.println("GL_NV_shader_atomic_float: " + caps.GL_NV_shader_atomic_float);
		System.out.println("GL_NV_shader_buffer_load: " + caps.GL_NV_shader_buffer_load);
		System.out.println("GL_NV_shader_buffer_store: " + caps.GL_NV_shader_buffer_store);
		System.out.println("GL_NV_tessellation_program5: " + caps.GL_NV_tessellation_program5);
		System.out.println("GL_NV_texgen_reflection: " + caps.GL_NV_texgen_reflection);
		System.out.println("GL_NV_texture_barrier: " + caps.GL_NV_texture_barrier);
		System.out.println("GL_NV_texture_compression_vtc: " + caps.GL_NV_texture_compression_vtc);
		System.out.println("GL_NV_texture_env_combine4: " + caps.GL_NV_texture_env_combine4);
		System.out.println("GL_NV_texture_expand_normal: " + caps.GL_NV_texture_expand_normal);
		System.out.println("GL_NV_texture_multisample: " + caps.GL_NV_texture_multisample);
		System.out.println("GL_NV_texture_rectangle: " + caps.GL_NV_texture_rectangle);
		System.out.println("GL_NV_texture_shader: " + caps.GL_NV_texture_shader);
		System.out.println("GL_NV_texture_shader2: " + caps.GL_NV_texture_shader2);
		System.out.println("GL_NV_texture_shader3: " + caps.GL_NV_texture_shader3);
		System.out.println("GL_NV_transform_feedback: " + caps.GL_NV_transform_feedback);
		System.out.println("GL_NV_transform_feedback2: " + caps.GL_NV_transform_feedback2);
		System.out.println("GL_NV_vertex_array_range: " + caps.GL_NV_vertex_array_range);
		System.out.println("GL_NV_vertex_array_range2: " + caps.GL_NV_vertex_array_range2);
		System.out.println("GL_NV_vertex_attrib_integer_64bit: " + caps.GL_NV_vertex_attrib_integer_64bit);
		System.out.println("GL_NV_vertex_buffer_unified_memory: " + caps.GL_NV_vertex_buffer_unified_memory);
		System.out.println("GL_NV_vertex_program: " + caps.GL_NV_vertex_program);
		System.out.println("GL_NV_vertex_program1_1: " + caps.GL_NV_vertex_program1_1);
		System.out.println("GL_NV_vertex_program2: " + caps.GL_NV_vertex_program2);
		System.out.println("GL_NV_vertex_program2_option: " + caps.GL_NV_vertex_program2_option);
		System.out.println("GL_NV_vertex_program3: " + caps.GL_NV_vertex_program3);
		System.out.println("GL_NV_vertex_program4: " + caps.GL_NV_vertex_program4);
		System.out.println("GL_NV_video_capture: " + caps.GL_NV_video_capture);
		System.out.println("\nGL_NVX_gpu_memory_info: " + caps.GL_NVX_gpu_memory_info);
		System.out.println("\nGL_SGIS_generate_mipmap: " + caps.GL_SGIS_generate_mipmap);
		System.out.println("GL_SGIS_texture_lod: " + caps.GL_SGIS_texture_lod);
		System.out.println("\nGL_SUN_slice_accum: " + caps.GL_SUN_slice_accum);
		System.out.println("\nOpenGL11: " + caps.OpenGL11);
		System.out.println("OpenGL12: " + caps.OpenGL12);
		System.out.println("OpenGL13: " + caps.OpenGL13);
		System.out.println("OpenGL14: " + caps.OpenGL14);
		System.out.println("OpenGL15: " + caps.OpenGL15);
		System.out.println("OpenGL20: " + caps.OpenGL20);
		System.out.println("OpenGL21: " + caps.OpenGL21);
		System.out.println("OpenGL30: " + caps.OpenGL30);
		System.out.println("OpenGL31: " + caps.OpenGL31);
		System.out.println("OpenGL32: " + caps.OpenGL32);
		System.out.println("OpenGL33: " + caps.OpenGL33);
		System.out.println("OpenGL40: " + caps.OpenGL40);
		System.out.println("OpenGL41: " + caps.OpenGL41);
		System.out.println("OpenGL42: " + caps.OpenGL42);

		System.out.println("\nGL RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		System.out.println("GL VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		System.out.println("GL VERSION: " + GL11.glGetString(GL11.GL_VERSION));
		
		if ( caps.OpenGL32 ) {
			IntBuffer buffer = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
	
			GL11.glGetInteger(GL32.GL_CONTEXT_PROFILE_MASK, buffer);
			int profileMask = buffer.get(0);
	
			System.out.println("\nPROFILE MASK: " + Integer.toBinaryString(profileMask));
	
			System.out.println("CORE PROFILE: " + ((profileMask & GL32.GL_CONTEXT_CORE_PROFILE_BIT) != 0));
			System.out.println("COMPATIBILITY PROFILE: " + ((profileMask & GL32.GL_CONTEXT_COMPATIBILITY_PROFILE_BIT) != 0));
		}*/
	}

/************************************UPDATE************************************************/
	
	private void update(){
/*
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			checkInput();
			RenderManager.render(bufferIds[0], bufferIds[2], 6);
			Globals.MANAGER.update();
			updateGameStates();
			Display.update();
			Display.sync(Globals.FPS);
		}
		Globals.MANAGER.cleanup();*/
	}

	private void checkInput() {
/*		while (Keyboard.next()){	
			F1 = Keyboard.isKeyDown(Keyboard.KEY_F1);
			F2 = Keyboard.isKeyDown(Keyboard.KEY_F2);
			F3 = Keyboard.isKeyDown(Keyboard.KEY_F3);
			F4 = Keyboard.isKeyDown(Keyboard.KEY_F4);
			F5 = Keyboard.isKeyDown(Keyboard.KEY_F5);
			F6 = Keyboard.isKeyDown(Keyboard.KEY_F6);
			TAB = Keyboard.isKeyDown(Keyboard.KEY_TAB);
			ESC = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
			if(TAB) {
				if(state == State.INTRO){
					setState(State.MAIN_MENU);
	            	Logger.getLogger(DisplayManager.class.getName()).log(Level.INFO, "Affichage du menu.");
				}else if(state == State.MAIN_MENU){
					setState(State.GAME);
	            	Logger.getLogger(DisplayManager.class.getName()).log(Level.INFO, "Affichage principal.");
				}else if(state == State.GAME){
					setState(State.MAP);
	            	Logger.getLogger(DisplayManager.class.getName()).log(Level.INFO, "Affichage de la carte.");
				}else if(state == State.MAP){
					setState(State.INTRO);
	            	Logger.getLogger(DisplayManager.class.getName()).log(Level.INFO, "Affichage de l'intro.");
				}
			}
			
			if (ESC){
				Globals.MANAGER.cleanup();
			}
			if (F1){
				Globals.MAP_RENDER_BATS=toggle(Globals.MAP_RENDER_BATS);
			}
			if (F2){
				Globals.MAP_RENDER_BLOCS=toggle(Globals.MAP_RENDER_BLOCS);
			}
			if (F3){
				Globals.MAP_RENDER_ROADS=toggle(Globals.MAP_RENDER_ROADS);
			}
			if (F4){
				Globals.MAP_RENDER_RAILS=toggle(Globals.MAP_RENDER_RAILS);
			}
			if (F5){
				Globals.MAP_RENDER_HEIGHT=toggle(Globals.MAP_RENDER_HEIGHT);
			}
			if (F6){
				Globals.MAP_RENDER_CANAL=toggle(Globals.MAP_RENDER_CANAL);
			}
		}
        if (Mouse.isButtonDown(0)) Mouse.setGrabbed(true);
        else if (Mouse.isButtonDown(1)) Mouse.setGrabbed(false);*/
    }

    private void updateGameStates() {
/*		switch (state) {
		case INTRO:
			
			renderIntro();
			break;
		case GAME:
			ParamsGlobals.MANAGER.renderMultiverse();
			renderGame();
			break;
		case MAIN_MENU:
			renderMainMenu();
			break;
		case MAP:
			if (ParamsGlobals.MAP_RENDER_MAP){
				ParamsGlobals.MANAGER.renderMap();
			}
			break;
		}		*/
	}

	/**Ici on gère l'affichage d'une introduction*/
	private void renderIntro(){
	}
	/**Ici on gère l'affichage de l'écran pricnipal*/
	private void renderGame(){
		renderGameUniverse();
		renderGameBats();
	}
	/**Ici on gère l'affichage du menu*/
	private void renderMainMenu(){
	}


	/*********************************RENDU********************************************/
	


	/** déclenche l'affichage de l'environnement non dépendant de la ville*/
	private void renderGameUniverse(){
		renderFloor();
		renderSky();
	}
	
	/** déclenche l'affichage des bâtiments*/
	private void renderGameBats(){
	}
	
	/** déclenche l'affichage du sol*/
	public void renderFloor(){
	
	}
	
	/** déclenche l'affichage du ciel*/
	private void renderSky(){
		
	}
	
	/****************************************UTILITY**************************************/
	
	private void setState(State state){
		Old_DisplayManager.state=state;
	}	
	
	private boolean toggle(boolean toggle){
		if (toggle) return false;
		else return true;
	}	
    


	private void exitOnGLError(String errorMessage) {
/*		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}*/
	}

}
